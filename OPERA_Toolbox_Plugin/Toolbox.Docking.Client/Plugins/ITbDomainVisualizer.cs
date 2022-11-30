using System.Threading.Tasks;
using Toolbox.Declarations;
using Toolbox.Docking.Client.Interfaces;
using Toolbox.Docking.Client.Services;

namespace Toolbox.Docking.Client.Plugins
{
    /// <summary>
    /// Plugin module for visualizing of domain.
    /// The classes which implements this interface must have these two attributes:
    ///      [Export([PLUGIN_NAME], typeof(ITbDomainVisualizer))]
    ///      [PartCreationPolicy(CreationPolicy.NonShared)]
    /// [PLUGIN_NAME] must correspond to server's implementation of: ITbQsarFactory.ClientDomainExplainer
    /// Modules are loaded using Managed Extensibility Framework (MEF).
    /// </summary>
    public interface ITbDomainVisualizer
    {
        /// <summary>
        /// Displays the domain for a Toolbox addin. 
        /// </summary>
        /// <param name="qsarId">The ID for the QSAR object for which a domain will be displayed</param>
        /// <param name="uiState">The item that will be explained by the Domain. If there is no chemical set the Domain is shown without example chemical.</param>
        /// <param name="clientServices">Toolbox services provider.</param>
        /// <returns>Returns the C# Task for async support.</returns>
        Task DisplayDomain(ITbObjectId qsarId, ITbUiState uiState, ITbClientServiceLocator clientServices);
    }
}