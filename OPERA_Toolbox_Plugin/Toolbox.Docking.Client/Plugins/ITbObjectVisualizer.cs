using System.Threading.Tasks;
using Toolbox.Declarations;
using Toolbox.Docking.Client.Interfaces;
using Toolbox.Docking.Client.Services;

namespace Toolbox.Docking.Client.Plugins
{
    /// <summary>
    /// Plugin module for visualizing of domain.
    /// The classes which implement this interface must have these two attributes:
    ///      [Export([PLUGIN_NAME], typeof(ITbDomainVisualizer))]
    ///      [PartCreationPolicy(CreationPolicy.NonShared)]
    /// [PLUGIN_NAME] must correspond to server's implementation of: ITbQsarFactory.ClientDomainExplainer
    /// Modules are loaded using Managed Extensibility Framework (MEF).
    /// </summary>
    public interface ITbObjectVisualizer
    {
        /// <summary>
        /// Displays the domain for a Toolbox addin. 
        /// </summary>
        /// <param name="objectId">The ID for the object for which a domain will be displayed</param>
        /// <param name="uiState">The current state, if needed for visualization.</param>
        /// <param name="clientServices">Toolbox services provider.</param>
        /// <returns>Returns the C# Task for async support.</returns>
        Task DisplayObject(ITbObjectId objectId, ITbUiState uiState, ITbClientServiceLocator clientServices);
    }
}