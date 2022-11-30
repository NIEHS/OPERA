using System;
using System.Text;
using System.Text.RegularExpressions;
using System.IO;
using System.Diagnostics;
using System.ComponentModel;
using System.Collections.Generic;
using Toolbox.Docking.Api.Control;
using Toolbox.Docking.Api.Data;
using Toolbox.Docking.Api.Objects;
using Toolbox.Docking.Api.Objects.Qsar;
using Toolbox.Docking.Api.Objects.Calculator;
using Toolbox.Docking.Api.Units;

namespace OperaAddin.Qsar
{
    public class QsarAddin : ITbQsar, ITbObjectDomain
    {
        private readonly Dictionary<string, string> _model;
        private readonly TbObjectId _objectId;
        private readonly TbScale _scale;

        private Dictionary<string, string> _predictedMapping;

        public QsarAddin(Dictionary<string, string> model, TbObjectId objectId, TbScale scale) {
            this._objectId = objectId;
            this._model = model;
            this._scale = scale;
        }

        public void Dispose() {
        }

        public TbScalarData Calculate(ITbBasket target) {
            if(!this._model.ContainsKey("Model Name")) {
                return new TbScalarData(new TbUnit(TbScale.EmptyRatioScale.Name, TbScale.EmptyRatioScale.BaseUnit), Double.NaN);
            }

            //Run the calculation
            if(_predictedMapping == null) {
                _predictedMapping = runOpera(target);
            }

            //Parse the prediction based on model
            string model = this._model["Model Name"];
            double predictedValue = double.NaN;
            string qualitativePrediction = "";
            if(model.Equals("CERAPP") || model.Equals("CoMPARA")) {
                if(_predictedMapping.ContainsKey("Binding category predicted")) { 
                    string predictedString = _predictedMapping["Binding category predicted"];
                    if(predictedString.Equals("Inactive")) {
                        qualitativePrediction = "Negative";
                    } else {
                        qualitativePrediction = "Positive";
                    }
                } else {
                    qualitativePrediction = "Negative";
                }
            } else if(model.Equals("CATMoS")) {
                if(_predictedMapping.ContainsKey("LD50 category predicted")) {
                     string predictedString = _predictedMapping["LD50 category predicted"];
                     predictedValue = double.Parse(predictedString);
                }
            } else if(model.Equals("pKa Acidic")) {
                if(_predictedMapping.ContainsKey("pKa Acidic Prediction")) {
                     string predictedString = _predictedMapping["pKa Acidic Prediction"];
                     predictedValue = double.Parse(predictedString);
                }
            } else if(model.Equals("pKa Basic")) {
                if(_predictedMapping.ContainsKey("pKa Basic Prediction")) {
                     string predictedString = _predictedMapping["pKa Basic Prediction"];
                     predictedValue = double.Parse(predictedString);
                }
            } else if (model.Equals("RBioDeg")) {
                if (_predictedMapping.ContainsKey("pred")) {
                    string predictedString = _predictedMapping["pred"];
                    if (predictedString.Equals("Inactive")) {
                        qualitativePrediction = "Not ready";
                    } else {
                        qualitativePrediction = "Ready";
                    }
                } else {
                    qualitativePrediction = "Not ready";
                }
            } else if (_predictedMapping.ContainsKey("pred")) {
                if (_predictedMapping.ContainsKey("pred")) {
                    string predictedString = _predictedMapping["pred"];
                    predictedValue = double.Parse(predictedString);
                }
            }
            //Anti log any models that return in log 10 to stay in 
            if(this._model["Log10"].Equals("TRUE")) {
                predictedValue = Math.Pow(10, predictedValue);
            }

            if(this._scale is TbQualitativeScale) {
                return new TbScalarData(new TbUnit(_scale.Name, qualitativePrediction), predictedValue);
            } else { 
                return new TbScalarData(new TbUnit(_scale.Name, ((TbRatioScale)_scale).BaseUnit), predictedValue);
            }
        }

        /**
         * Does the qsar prediction and creates the necessary data structures for the plugin to display the information
         */
        public ITbPrediction Predict(ITbBasket target) {
            var predictedScalarData = Calculate(target);
            var predictedTbData = new TbData(predictedScalarData.Unit, predictedScalarData.Value);
            string model = this._model["Model Name"];

            //Add additional string based metadata to output
            Dictionary<string, string> AdditionalMetadata = new Dictionary<string, string>();
            addToMetadata(ref AdditionalMetadata, "Endpoint", false);
            addToMetadata(ref AdditionalMetadata, "Route of administration", false);
            addToMetadata(ref AdditionalMetadata, "Test organisms (species)", false);
            addToMetadata(ref AdditionalMetadata, "Prediction Range", true);

            //Add additional numerical data to the output
            Dictionary<string, TbData> AdditionalPredictions = new Dictionary<string, TbData>();
            if(model.Equals("CERAPP") || model.Equals("CoMPARA")) {
                //Specific parsing for CERAPP and CoMPARA models
                addToMetadata(ref AdditionalMetadata, "Agonist category predicted", true);
                addToMetadata(ref AdditionalMetadata, "Antagonist category predicted", true);
                addToMetadata(ref AdditionalMetadata, "Binding category predicted", true);
            } else if(model.Equals("CATMoS")) {
                //Specific parsing for CATMoS
                addToPredictions(ref AdditionalPredictions, model, "EPA category predicted");
                addToPredictions(ref AdditionalPredictions, model, "GHS category predicted");
            } else if(model.Equals("pKa Acidic")) {
                //Specific parsing for pKa
                addToPredictions(ref AdditionalPredictions, model, "pKa Acidic Prediction");
                addToMetadata(ref AdditionalMetadata, "pKa Acidic Prediction Range", true);
            } else if(model.Equals("pKa Basic")) {
                //Specific parsing for pKa
                addToPredictions(ref AdditionalPredictions, model, "pKa Basic Prediction");
                addToMetadata(ref AdditionalMetadata, "pKa Basic Prediction Range", true);
            } else if(model.Equals("LogD")) {
                addToPredictions(ref AdditionalPredictions, model, "pH 5.5 predicted");
                addToPredictions(ref AdditionalPredictions, model, "pH 7.4 predicted");
                addToMetadata(ref AdditionalMetadata, "pH 5.5 Prediction Range", true);
                addToMetadata(ref AdditionalMetadata, "pH 7.4 Prediction Range", true);
            } else if(model.Equals("RBioDeg")) {
                addToMetadata(ref AdditionalMetadata, "pred", true);
            }
            //Generic parsing for all of the models
            addToPredictions(ref AdditionalPredictions, model, "AD Index");
            addToPredictions(ref AdditionalPredictions, model, "Confidence Index");
            

            var predictionDescription = new TbMetadata(AdditionalMetadata, AdditionalPredictions);
            TbData Mockdescriptordata = new TbData(new TbUnit(TbScale.EmptyRatioScale.FamilyGroup, TbScale.EmptyRatioScale.BaseUnit), new double?());
            Dictionary<TbObjectId, TbData> matrixdescriptorvalues = new Dictionary<TbObjectId, TbData>()
            {
                {
                this._objectId,
                Mockdescriptordata
                }
            };
            return new PredictionAddin(predictedTbData, predictionDescription, matrixdescriptorvalues, null);
        }

        public bool IsRelevantToChemical(ITbBasket chemical, out string reason) {
            reason = null;
            return true;
        }
        
        /**
         * Checks the domain based on the output of the OPERA calculation
         */
        public TbDomainStatus CheckDomain(ITbBasket basket) {
            if(_predictedMapping == null) {
                _predictedMapping = runOpera(basket);
            }
            if(_predictedMapping == null || !_predictedMapping.ContainsKey("AD")) {
                return TbDomainStatus.Undefined;
            } else if(_predictedMapping["AD"].Contains("inDomain")) {
                return TbDomainStatus.InDomain;
            } else if(_predictedMapping["AD"].Contains("outOfDomain")) {
                return TbDomainStatus.OutOfDomain;
            } else {
                return TbDomainStatus.Undefined;
            }
        }

        /**
         * Runs OPERA on the command line with the given chemical and model and parses the output into a dictionary
         */
        private Dictionary<string, string> runOpera(ITbBasket target) {
            if(!this._model.ContainsKey("Model Name")) {
                return null;
            }
            string model = this._model["Model Name"];

            string path = System.Environment.GetEnvironmentVariable("USERPROFILE") + "/OPERA_Plugin_Temp";
            Directory.CreateDirectory(path);

            string smiles = target.Chemical.Smiles;
            string strCmdText;
            string operaPath = QsarAddinDefinitions.getOperaPath();

            string inputPath = path + "\\" + model.Replace(" ", "_") + ".smi";

            using (FileStream fs = File.Create(inputPath))
            {
                byte[] info = new UTF8Encoding(true).GetBytes(smiles);
                fs.Write(info, 0, info.Length);
            }

            //Make a new string for the model to run to deal with pKa case where there are 2 models that need to run the same model
            string runModel = model;
            if(model.Contains("pKa")) {
                runModel = "pKa";
            }
            
            strCmdText = "/C " + operaPath + " -s " + inputPath + " -e " + runModel + " -stdout -v 0";
            Process process = new Process
            {
                StartInfo =
                {
                    UseShellExecute = false,
                    RedirectStandardOutput = true,
                    RedirectStandardError = true,
                    CreateNoWindow = true,
                    FileName = "cmd.exe",
                    Arguments = strCmdText
                }
            };
            process.Start();
            process.WaitForExit();

            StreamReader reader = process.StandardOutput;
            string output = reader.ReadToEnd();

            return parseOutput(output, model);
        }

        /**
         * Use regex to parse the command line output of OPERA
         * @output The full string output given by running OPERA in command line
         * @model The name of the model
         */
        private Dictionary<string, string> parseOutput(string output, string model) {
            Dictionary<string, string> predictions = new Dictionary<string, string>();

            //Model specific parsing
            if (model.Contains("CERAPP") || model.Contains("CoMPARA")) {
                string agoPattern = @"Agonist category predicted= (.*)";
                useRegex(ref predictions, output, "Agonist category predicted", agoPattern);

                string antaPattern = @"Antagonist category predicted= (.*)";
                useRegex(ref predictions, output, "Antagonist category predicted", antaPattern);

                string bindPattern = @"Binding category predicted= (.*)";
                useRegex(ref predictions, output, "Binding category predicted", bindPattern);
            } else if (model.Equals("CATMoS")) {
                string epaPattern = @"EPA category predicted= ([\d]*)";
                useRegex(ref predictions, output, "EPA category predicted", epaPattern);

                string ghsPattern = @"GHS category predicted= ([\d]*)";
                useRegex(ref predictions, output, "GHS category predicted", ghsPattern);

                string ld50Pattern = @"LD50 predicted= ([\d\.]*)";
                useRegex(ref predictions, output, "LD50 category predicted", ld50Pattern);
            } else if (model.Contains("pKa")) {
                string predictedValues = @"pKa acidic and basic predicted= (.*)";
                Regex regex = new Regex(predictedValues);
                Match m = regex.Match(output);
                string match = "";
                if (m.Success) {
                    match = m.Groups[1].Value.Trim();
                }
                string[] predictedArray = match.Split(',');
                predictions.Add("pKa Acidic Prediction", predictedArray[0].Trim());
                predictions.Add("pKa Basic Prediction", predictedArray[1].Trim());


                string predictedRangeValues = @"pKa acidic and basic predRange: (.*)";
                regex = new Regex(predictedRangeValues);
                m = regex.Match(output);
                if (m.Success) {
                    match = m.Groups[1].Value.Trim();
                }
                predictedArray = match.Split(',');
                predictions.Add("pKa Acidic Prediction Range", predictedArray[0].Trim());
                predictions.Add("pKa Basic Prediction Range", predictedArray[1].Trim());
            } else if (model.Equals("LogD")) {
                string ph55Pattern = @"LogD pH 5.5 predicted= (.*)";
                useRegex(ref predictions, output, "pH 5.5 predicted", ph55Pattern);

                string ph55RangePattern = @"LogD pH 5.5 predRange: (.*)";
                useRegex(ref predictions, output, "pH 5.5 Prediction Range", ph55RangePattern);

                string ph74Pattern = @"LogD pH 7.4 predicted= (.*)";
                useRegex(ref predictions, output, "pH 7.4 predicted", ph74Pattern);

                string ph74RangePattern = @"LogD pH 7.4 predRange: (.*)";
                useRegex(ref predictions, output, "pH 7.4 Prediction Range", ph74RangePattern);
            } else {
                string predPattern = @"predicted= (.*)";
                useRegex(ref predictions, output, "pred", predPattern);
            }

            if(!(model.Contains("pKa") || model.Equals("LogD"))) {
                string predRangePattern = @"predRange: (.*)";
                useRegex(ref predictions, output, "Prediction Range", predRangePattern);
            }

            //Generic parsing
            string ADPattern = @"AD: (.*)";
            useRegex(ref predictions, output, "AD", ADPattern);

            string ADIndexPattern = @"AD_index= (.*)";
            useRegex(ref predictions, output, "AD Index", ADIndexPattern);

            string confIndexPattern = @"Conf_index= (.*)";
            useRegex(ref predictions, output, "Confidence Index", confIndexPattern);

            return predictions;
        }

        /**
         * Helper method to match regex string and add results to the dictionary
         * @predictions The dictionary the prediction needs to be added to
         * @text The text to parse with the regex
         * @key The key to add the prediction to in the dictionary
         * @regexString The regex to use to parse the text with
         */
        private void useRegex(ref Dictionary<string, string> predictions, string text, string key, string regexString) {
            Regex regex = new Regex(regexString);
            Match m = regex.Match(text);
            string match = "";
            if (m.Success) {
                match = m.Groups[1].Value.Trim();
            }
            if(!match.Equals(""))
                predictions.Add(key, match);
        }

        /**
         * Helper method that adds the given values to the metadata dictionary
         * @dictionary The dictionary the metadata needs to be added to
         * @key The key to add the prediction to in the dictionary
         * @predicted Whether or not the metadata was predicted or comes from model data
         */
        private void addToMetadata(ref Dictionary<string, string> dictionary, string key, bool predicted) {
            if(predicted) {
                if(_predictedMapping.ContainsKey(key)) {
                    //For prediction ranges we need to antilog the models that
                    //have log values just like we do with the predictions
                    if(key.Contains("Prediction Range") && this._model["Log10"].Equals("TRUE")) {
                        try {
                            var predRange = _predictedMapping[key].Split(':');
                            string lowRange = round(Double.Parse(predRange[0].Replace("[", "")));
                            string highRange = round(Double.Parse(predRange[1].Replace("]", "")));
                            dictionary.Add(key, "[" + lowRange + ":" + highRange + "]");
                        } catch(Exception e) {
                            dictionary.Add(key, _predictedMapping[key]);
                        }
                    } else {
                        //Default to the original string
                        dictionary.Add(key, _predictedMapping[key]);
                    }
                }
            } else {
                if(this._model.ContainsKey(key) && !this._model[key].Equals(""))
                    dictionary.Add(key, this._model[key]);
            }
        }

        /**
         * Helper method to round values for display
         * @value The value to round
         */
        private string round(double value) {
            string strValue = "";
            //Display very small and very large numbers in scientific notation
            if(value < -3 || value > 6) {
                value = Math.Pow(10, value);
                strValue = string.Format("{0:#.##E+00}", value);
            } else {
                value = Math.Round(Math.Pow(10, value), 3, MidpointRounding.AwayFromZero);
                strValue = "" + value;
            }
            return strValue;
        }

         /**
         * Helper method that adds the given values to the predictions dictionary
         */
        private void addToPredictions(ref Dictionary<string, TbData> dictionary, string model, string key) {
            if(_predictedMapping.ContainsKey(key))
            {
                double value = double.NaN;
                try {
                    value = Double.Parse(_predictedMapping[key]);
                } catch(Exception e) {
                    value = double.NaN;
                }
                dictionary.Add(key, new TbData(new TbUnit(TbScale.EmptyRatioScale.Name, string.Empty), value));
            }
        }
    }
}
