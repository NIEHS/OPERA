using System;
using System.Collections.Generic;
using System.Linq;
using Toolbox.Docking.Api.Chemical;
using Toolbox.Docking.Api.Control;
using Toolbox.Docking.Api.Data;
using Toolbox.Docking.Api.Objects;
using Toolbox.Docking.Api.Objects.Qsar;
using Toolbox.Docking.Api.Units;

namespace OperaAddin.Qsar
{
    public class TbQsarAddinFactory : ITbQsarFactory, ITbObjectFactory, ITbObjectFactoryDomain
    {
        //private fields
        private Dictionary<string, string> _modelData;

        private string _operaVersion;

        //public properties
        public TbObjectFlags Flags { get; }

        public QsarFlags QsarFlags => QsarFlags.None;

        public TbObjectId ObjectId { get; }

        public TbObjectAbout ObjectAbout { get; }

        public string ClientDomainExplainer => "OEPRA Domain Visualizer";
        
        public TbMetadata Metadata { get; }

        public string AgreementInfo => null;

        public string ReportDisclaimer => null;

        public IReadOnlyList<string> EndpointLocation { get; }

        public TbScale ScaleDeclaration { get; }

        public IReadOnlyList<QsarDescriptorInfo> XDescriptors { get; private set; }

        public string QmrfLocation { get; private set; }

        public TbQsarStatistics Statistics
        {
            get
            {
                return QsarAddinDefinitions.M4RatioModelStatistics(this._modelData);
            }
        }

        /**
         * Constructor for QsarAddinFactory, contains all the necessary information for each model
         * @model The dictionary containing information about the model
         * @operaVersion The version of OPERA obtained from the command line
         */
        public TbQsarAddinFactory(Dictionary<string,string> Model, string operaVersion)
        {
            _modelData = Model;
            _operaVersion = operaVersion;

            //The MUST preliminary initialize properties of IObjectFactory
            Flags = TbObjectFlags.None;

            string[] version = null;
            if(Model.ContainsKey("Version") && !Model["Version"].Equals(""))
                version = Model["Version"].Trim('v').Split('.');

            if(version == null || version.Length != 2) {
                version = new string[] {"1", "0"};
            }

            string modelName = "";
            if(Model.ContainsKey("Model Name"))
                modelName = Model["Model Name"];
            
            ObjectId = new TbObjectId("OPERA " + modelName, new Guid(Model["Guid"]), new Version(Int32.Parse(version[0]), Int32.Parse(version[1])));

            ObjectAbout = QsarAddinDefinitions.GetM4ObjectAbout(_operaVersion);
            
            List<string> endpointLocations = new List<string>();
            if(Model.ContainsKey("Endpoint 1") && !Model["Endpoint 1"].Equals(""))
            {
                endpointLocations.Add(Model["Endpoint 1"]);
                if(Model.ContainsKey("Endpoint 2") && !Model["Endpoint 2"].Equals(""))
                {
                    endpointLocations.Add(Model["Endpoint 2"]);
                    if(Model.ContainsKey("Endpoint 3") && !Model["Endpoint 3"].Equals(""))
                    {
                        endpointLocations.Add(Model["Endpoint 3"]);
                        if(Model.ContainsKey("Endpoint 4") && !Model["Endpoint 4"].Equals(""))
                        {
                            endpointLocations.Add(Model["Endpoint 4"]);
                        }
                    }
                }
            }

            EndpointLocation = endpointLocations;

            Metadata = new TbMetadata((IReadOnlyDictionary<string, string>)QsarAddinDefinitions.getMetaDataValues(_modelData), null);
            
            var scale = QsarAddinDefinitions.ReturnScale(_modelData);

            ScaleDeclaration = scale;
        }

        public bool InitFactory(IList<string> errorLog, out int? hash, ITbInitTask initTask)
        {
            //Set the QMRF if it exists for the model
            if(_modelData.ContainsKey("QMRF"))
            {
                string path = System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetExecutingAssembly().Location);
                QmrfLocation = path + "\\OPERA_QMRF\\" + _modelData["QMRF"].Substring(5) + ".pdf";
            } else { 
                QmrfLocation = "";
            }

            hash = null;
            return true;
        }

        public IReadOnlyList<ChemicalWithData> TrainingSet(ITbWorkTask task)
        {
            return QsarAddinDefinitions.GetSet(this._modelData, this.ScaleDeclaration, true);
        }

        public IReadOnlyList<ChemicalWithData> GetTestSet(ITbWorkTask task)
        {
           return QsarAddinDefinitions.GetSet(this._modelData, this.ScaleDeclaration, false);
        }

        public ITbQsar GetQsar(ITbWorkTask task)
        {
            return new QsarAddin(this._modelData, this.ObjectId, this.ScaleDeclaration);
        }

        public IReadOnlyList<ISuportingChemicals> AdditionalLists(ITbWorkTask task)
        {
            return null;
        }
        
    }
}
