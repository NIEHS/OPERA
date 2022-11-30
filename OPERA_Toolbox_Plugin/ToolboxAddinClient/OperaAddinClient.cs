using System;
using System.Threading.Tasks;
using System.ComponentModel.Composition;
using Toolbox.Declarations;
using Toolbox.Docking.Client.Interfaces;
using Toolbox.Docking.Client.Plugins;
using Toolbox.Docking.Client.Services;

namespace Toolbox.Addin.OPERA.ClientSide
{
    [Export("OEPRA Domain Visualizer", typeof(ITbDomainVisualizer))]
    [PartCreationPolicy(CreationPolicy.NonShared)]
    public class OperaAddinClient : ITbDomainVisualizer
    {

        public OperaAddinClient()
        {

        }

        public Task DisplayDomain(ITbObjectId qsarId, ITbUiState uiState, ITbClientServiceLocator clientServices)
        { 
            string domainMessage = "No domain explanation available.";
            clientServices.DialogService.ShowMessage(domainMessage, "Domain information");

            return Task.FromResult("");
        }
    }
}
