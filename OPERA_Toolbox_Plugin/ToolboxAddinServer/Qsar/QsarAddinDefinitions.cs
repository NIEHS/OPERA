using System;
using System.IO;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Toolbox.Docking.Api.Chemical;
using Toolbox.Docking.Api.Data;
using Toolbox.Docking.Api.Objects;
using Toolbox.Docking.Api.Objects.Qsar;
using Toolbox.Docking.Api.Units;
using System.Linq;

namespace OperaAddin.Qsar
{
    public static class QsarAddinDefinitions
    {
        public static Tuple<string, TbData> GetDurationKeyValuePair(double value, string unit)
        {
            var durationTbData = new TbData(new TbUnit(TbScale.Time.Name, unit), value);
            return new Tuple<string, TbData>("Duration", durationTbData);
        }

        /**
         * Create a ditionary of metadata values based on what information the model has available
         * @model The dictionary containing information about the model
         */
        public static Dictionary<string, string> getMetaDataValues(Dictionary<string, string> Model) {
            string[] Metadatalist = new string[] {
                "Endpoint",
                "Test organisms (species)",
                "Route of administration"};
            Dictionary<string, string> dict = new Dictionary<string, string>();
            foreach (string Colname in Metadatalist) { 
                if (Model[Colname] != "") {
                    dict.Add(Colname, Model[Colname]);
                }
            }

            return dict;
        }

        /**
         * Returns the scale containing unit information for the given model
         * @model The dictionary containing information about the model
         */
        public static TbScale ReturnScale(Dictionary<string, string> Model) {
            string modelName = Model["Model Name"];
            if(modelName.Equals("BP") || modelName.Equals("MP")) {
                return new TbRatioScale(TbScale.Temperature, TbScale.Temperature.BaseUnit);
            } else if(modelName.Equals("VP")) {
                return new TbRatioScale(TbScale.Pressure, Model["Unit"]);
            } else if(modelName.Equals("HL")) {
                return new TbRatioScale(TbScale.PressurePerMole, Model["Unit"]);
            }  else if(modelName.Equals("WS")) {
                return new TbRatioScale(TbScale.MolarConcentration, Model["Unit"]);
            } else if(modelName.Equals("BioDeg") || modelName.Equals("KM")) {
                return new TbRatioScale(TbScale.Time, Model["Unit"]);
            } else if(modelName.Equals("KOC")) {
                return new TbRatioScale(TbScale.SpecificVolume, Model["Unit"]);
            } else if(modelName.Equals("AquaTox")) {
                return new TbRatioScale(TbScale.MolarConcentration, Model["Unit"]);
            } else if(modelName.Equals("CATMoS")) {
                return new TbRatioScale(TbScale.AdministeredDose_mass, Model["Unit"]);
            } else if(modelName.Equals("RBioDeg")) {
                return TbScale.BiodegradabilityCategories;
            } else if(modelName.Equals("AOH")) {
                return new TbRatioScale("AOH unit", "AOH", new Guid("359968a3-d0a6-49e6-97ff-920623fdc0d3"), Model["Unit"]);
            } else if(modelName.Equals("CERAPP")) {
                List<string> labels = new List<string>();
                labels.Add("Positive");
                labels.Add("Negative");
                return new TbOrdinalScale("Estrogen Receptors", "Estrogen", new Guid("6f0408fc-9b24-405b-92be-a3cd12020b93"), labels);
            } else if(modelName.Equals("CoMPARA")) {
                List<string> labels = new List<string>();
                labels.Add("Positive");
                labels.Add("Negative");
                return new TbOrdinalScale("Androgen Receptors", "Androgen", new Guid("69d4f8ff-3a5c-4919-8f49-d55f755c9e60"), labels);
            } else if(modelName.Equals("Caco2")) {
                return new TbRatioScale("Speeds", "Speed", new Guid("58d16850-1053-4d4c-ac3b-411f87a47189"), "log(cm/s)");
            } else {
                return TbRatioScale.EmptyRatioScale;
            }
        }

        /**
         * Get the training or test set data of the given model
         */
        public static IReadOnlyList<ChemicalWithData> GetSet(Dictionary<string, string> Model, TbScale ScaleDeclaration, bool train)
        {
            string modelName = Model["Model Name"];

            int[] useless;
            return parseSDF(Model, train, out useless);
        }

        /**
         * Get the training or test set counts for the given model
         * @model The dictionary containing information about the model
         */
        public static TbQsarStatistics M4RatioModelStatistics(Dictionary<string, string> Model) {
            string modelName = Model["Model Name"];
            
            int[] trainTestCounts;
            parseSDF(Model, false, out trainTestCounts);

            return new TbQsarStatistics(trainTestCounts[0], trainTestCounts[0], trainTestCounts[1], trainTestCounts[1]);   
        }

        public static TbObjectAbout GetM4ObjectAbout(string version)
        {
            return new TbObjectAbout
            (
                description:
                @"OPERA is a free and open-source/open-data application available in command line and user-friendly graphical interface, providing QSAR models predictions as well as applicability domain and accuracy assessment for physicochemical properties, ADME, environmental fate, and toxicological endpoints." + "\n" +
                @"This plugin requires OPERA command line to be installed on your machine to function properly. OPERA command line can be installed from the github link here: https://github.com/NIEHS/OPERA/" + "\n" +
                "Current version " + version + "\n" +
                "AD: global applicability domain assessment (in domain/out of Domain) based on the whole training set.\n" +
                "AD index: local applicability domain index (0-1 range) based on the similarity to the five nearest neighbors.\n" +
                "Confidence index: accuracy assessment index (0-1 range) based on the five nearest neighbors.\n" +
                "Prediction range: confidence range around the predicted value based on the accuracy estimate and the inherent variability of the data.\n" +
                "More information available in QMRFs.",
                donator: "NIEHS/NICEATM",
                disclaimer: null,
                authors: "Mansouri, Kamel",
                url: @"https://github.com/NIEHS/OPERA",
                name: "OPERA Models",
                helpFile: null,
                additionalInfo: new[]
                {
                    new TbObjectAboutTextPair("Adopted", "Toolbox 4.5 March 2022"), 
                }
            );
        }

        /**
         * Parses the SDF files to get the training and testing chemical sets from OPERA data files
         * @model The dictionary containing information about the model
         * @train True to return the training set and false to return the testing set
         * @trainTestCounts Array containing the total counts of both the training and testing sets
         */
        private static List<ChemicalWithData> parseSDF(Dictionary<string, string> model, bool train, out int[] trainTestCounts)
        {
            string modelName = model["Model Name"];
            List<ChemicalWithData> chemList = new List<ChemicalWithData>();

            trainTestCounts = new int[] {0,0};
            int trainCount = 0;
            int testCount = 0;


            string fileName = "";
            //Exclude parsing the models that have too many 
            if(modelName.Equals("CATMoS") || modelName.Equals("CoMPARA") || modelName.Equals("CERAPP") || modelName.Equals("Caco2") || modelName.Equals("LogD")) {
                return new List<ChemicalWithData>();    
            } else if(modelName.Equals("FuB")) {
                fileName = "FU_QR.sdf";
            } else if(modelName.Contains("pKa")) {
                fileName = "pKa_QR.sdf";
            } else {
                fileName = modelName + "_QR.sdf";
            }
            string path = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location) + "\\OPERA_Data\\" + fileName;

            string text = File.ReadAllText(path);
            string[] splitText = text.Split(new string[] { "$$$$" }, StringSplitOptions.None);

            //Set up regex patterns to parse file
            string trainPattern = @"> <Tr_1_Tst_0>\r\n(.*)";
            Regex trainRegex = new Regex(trainPattern);
            string casPattern = @"> <CAS>\r\n(.*)";
            Regex casRegex = new Regex(casPattern);
            string smilePattern = @"> <Original_SMILES>\r\n(.*)";
            Regex smileRegex = new Regex(smilePattern);
            string namePattern = @"> <preferred name>\r\n(.*)";
            Regex nameRegex = new Regex(namePattern);
            string valueString = model["Data Labels"];
            string valuePattern = @"> <" + valueString +">\r\n(.*)";
            Regex valueRegex = new Regex(valuePattern);

            //Loop through all the chemicals
            for (int i = 0; i < splitText.Length; i++) {
                long cas = 0;
                string casString = "";
                string smile = "";
                string name = "";
                string value = "";

                //Match training/testing data
                Match m = trainRegex.Match(splitText[i]);
                if (m.Success) {
                    string trainString = m.Groups[1].Value.Trim();

                    if(trainString.Equals("0"))
                        testCount++;
                    if(trainString.Equals("1"))
                        trainCount++;

                    if (!((trainString.Equals("1") && train) || (trainString.Equals("0") && !train))) {
                        continue;
                    }
                } else {
                    continue;
                }
                //Match cas number
                m = casRegex.Match(splitText[i]);
                if (m.Success) {
                    casString = m.Groups[1].Value;
                    if(casString.Contains("|")) {
                        casString = casString.Split('|')[0];
                    }
                    if(casString.Contains("NOCAS_")) {
                        casString = casString.Replace("NOCAS_", "");
                    }
                    casString = casString.Replace("-", "").Trim();
                    try { 
                        cas = long.Parse(casString);
                    } catch(Exception e) {
                        cas = 0;
                    }
                }
                //Match smiles string
                m = smileRegex.Match(splitText[i]);
                if (m.Success) {
                    smile = m.Groups[1].Value.Trim();
                }
                //Match chemical name
                m = nameRegex.Match(splitText[i]);
                if (m.Success) {
                    name = m.Groups[1].Value.Trim();
                }
                //Match output value
                m = valueRegex.Match(splitText[i]);
                if (m.Success) {
                    value = m.Groups[1].Value.Trim();
                }

                List<string> names = new List<string>();
                names.Add(name);

                var scale = QsarAddinDefinitions.ReturnScale(model);
                var _calcUnit = new TbUnit(TbScale.EmptyRatioScale.FamilyGroup, TbScale.EmptyRatioScale.BaseUnit);
                if(scale.GetType().Equals(typeof(TbRatioScale))) {
                    var ratioScale = (TbRatioScale)scale;
                    _calcUnit = new TbUnit(ratioScale.Name, ratioScale.BaseUnit);
                }
                Double valueNumber = 0;
                Double.TryParse(value, out valueNumber);
                if(model["Log10"].Equals("TRUE")) {
                    valueNumber = Math.Pow(10, valueNumber);
                }
                TbData Mockdescriptordata = new TbData(new TbUnit(TbScale.EmptyRatioScale.FamilyGroup, TbScale.EmptyRatioScale.BaseUnit), new double?());
                ChemicalWithData chem = new ChemicalWithData(cas, names, smile, 
                    new TbDescribedData[] { new TbDescribedData(Mockdescriptordata, null) }, 
                    new TbDescribedData(new TbData(_calcUnit, valueNumber), null));
                chemList.Add(chem);
            }

            trainTestCounts = new int[] {trainCount, testCount};

            return chemList;
        }

        /**
         * Gets the path for opera first looking for the generic exe before looking for the parallelized version
         * If neither of these exist within the users environment variables return the default install location of OPERA
         */
        public static string getOperaPath()
        {
            string operaPath = QsarAddinDefinitions.locateEXEInPath("OPERA.exe");
            if (operaPath.Equals(String.Empty))
                operaPath = QsarAddinDefinitions.locateEXEInPath("OPERA_P.exe");
            if (operaPath.Equals(String.Empty))
                operaPath = "\"C:\\Program Files\\OPERA\\application\\opera\"";

            return operaPath;
        }

        /**
         * Locates and returns the path for the given exe within the users environment variables
         * @filename The exe filename to search for
         */
        public static string locateEXEInPath(String filename)
        {
            String path = Environment.GetEnvironmentVariable("path");
            Console.WriteLine(path);
            String[] folders = path.Split(';');
            foreach (String folder in folders)
            {
                if (File.Exists(folder + filename))
                {
                    return "\"" + folder + filename + "\"";
                }
                else if (File.Exists(folder + "\\" + filename))
                {
                    return "\"" + folder + "\\" + filename + "\"";
                }
            }

            return String.Empty;
        }
    }
}
