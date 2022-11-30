using System;
using System.IO;
using System.Linq;
using System.Diagnostics;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Toolbox.Docking.Api;
using Toolbox.Docking.Api.Objects;
using System.Windows.Forms;
using OperaAddin.Qsar;

namespace OperaAddin
{
    public class OperaQSAR : IToolboxAddin
    {
        public IEnumerable<ITbObjectFactory> GetToolboxObjectFactories()
        {
            List<ITbObjectFactory> toolboxObjectFactories = new List<ITbObjectFactory>();

            //Get OPERA version from command line
            string operaPath = QsarAddinDefinitions.getOperaPath();
            string strCmdText = "/C " + operaPath + " --version";
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
            string text = reader.ReadToEnd();

            if(text.Contains("Version")) {
                Regex versionRegex = new Regex(@"\d+(\.\d+)+");
                Match m = versionRegex.Match(text);
                string version = "Not Installed";
                if (m.Success) {
                    version = m.Groups[0].Value.Trim();
                }
                Dictionary<string, Dictionary<string, string>> OperaModels = RetrieveModelInfo(System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location));
                foreach (string model in OperaModels.Keys)
                    toolboxObjectFactories.Add((ITbObjectFactory)new TbQsarAddinFactory(OperaModels[model], version));
            }
            
            return (IEnumerable<ITbObjectFactory>)toolboxObjectFactories;
        }

        /**
         * Parse the tsv file with model information into a dictionary
         */
        private Dictionary<string, Dictionary<string, string>> RetrieveModelInfo(string path)
        {
            var lines = File.ReadAllLines(path + "/OPERA_models_plugin.tsv");

            string[] columnHeaders = lines[0].Split('\t');

            Dictionary<string, Dictionary<string, string>> Models = new Dictionary<string, Dictionary<string, string>>();

            foreach (var line in lines.Skip(1))
            {
                var newDict = new Dictionary<string, string>();

                var cells = line.Split('\t');

                string model = cells[0];

                for (int i = 0; i < columnHeaders.Length; i++)
                {
                    newDict.Add(columnHeaders[i], cells[i]);
                }

                Models.Add(model, newDict);
            }

            //Add additional model information from OPERA command line
            addCommandLineInfo(Models, path);

            return Models;
        }

        /**
         * Run the OPERA -vm command to get additional information about the models to add to the dictionary
         */
        private Dictionary<string, Dictionary<string, string>> addCommandLineInfo(Dictionary<string, Dictionary<string, string>> Models, string path)
        {
            string operaPath = QsarAddinDefinitions.getOperaPath();
            string strCmdText = "/C " + operaPath + " -vm";
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
            string text = reader.ReadToEnd();

            if(!text.Contains("Error using OPERA")) { 
                string[] sections = Regex.Split(text, "\\n\\n");

                foreach (var section in sections)
                {
                    var lines = section.Split('\n');

                    string endpoint = lines[0];
                    if (endpoint.Contains("For more information"))
                        continue;


                    for (int i = 1; i < lines.Length; i++)
                    {
                        var data = lines[i].Split('\t');
                        string Model = data[0].Trim();
                        List<string> modelList = new List<string>();
                        //CERAPP and CoMPARA are displayed in two different parts of the endpoint tree so add them twice
                        if(Model.Equals("ER")) {
                            modelList.Add("CERAPP");
                            modelList.Add("CERAPP_2");
                        } else if(Model.Equals("AR")) {
                            modelList.Add("CoMPARA");
                            modelList.Add("CoMPARA_2");
                        } else if(Model.Equals("AcuteTox")) { 
                            modelList.Add("CATMoS");
                        } else {
                            modelList.Add(Model);
                        }
                    
                        foreach(string modelName in modelList) { 
                            if(!Models.ContainsKey(modelName))
                            {
                                continue;
                            }
                            var modelDict = Models[modelName];

                            modelDict.Add("Version", data[1].Trim());
                            //Some models don't have an associated qmrf
                            if(data.Length > 3)
                                modelDict.Add("QMRF", data[3].Trim());
                        }
                    }
                }
            }
            return Models;
        }
    }
}
