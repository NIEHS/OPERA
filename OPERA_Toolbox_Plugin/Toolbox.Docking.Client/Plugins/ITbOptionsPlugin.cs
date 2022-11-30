using System.Threading.Tasks;
using Toolbox.Declarations;
using Toolbox.Docking.Client.Interfaces;
using Toolbox.Docking.Client.Services;

namespace Toolbox.Docking.Client.Plugins
{

    /// <summary>
    /// Plugin module for explaining results.
    /// The classes which implements this interface must have these two attributes:
    ///      [Export([PLUGIN_NAME], typeof(ITbOptionsPlugin))]
    ///      [PartCreationPolicy(CreationPolicy.NonShared)]
    /// [PLUGIN_NAME] must correspond to server's implementation of: ITbObjectFactoryTunable.ClientOptionsModule
    /// Modules are loaded using Managed Extensibility Framework (MEF).
    /// </summary>
    public interface ITbOptionsPlugin
    {
        /// <summary>
        /// Display optionf for a Toolbox object.
        /// </summary>
        /// <param name="uiState">Current UI state for the Toolbox Client</param>
        /// <param name="tbObjectId">The Toolbox object ID</param>
        /// <param name="options">Options, encoded as string, to be displayed.</param>
        /// <param name="clientServices">Service locator object.</param>
        /// <returns>Return new options or null</returns>
        Task<string> ExecuteOptions(ITbUiState uiState, ITbObjectId tbObjectId, string options,
            ITbClientServiceLocator clientServices);
    }
}