using System.Threading.Tasks;
using Toolbox.Docking.Client.Interfaces;
using Toolbox.Docking.Client.Services;

namespace Toolbox.Docking.Client.Plugins
{   
    /// <summary>
    /// Plugin module for explaining results.
    /// The classes which implements this interface must have these two attributes:
    ///      [Export([PLUGIN_NAME], typeof(IToolboxPlugin))]
    ///      [PartCreationPolicy(CreationPolicy.NonShared)]
    /// [PLUGIN_NAME] must correspond to server's implementation of: ITbObjectFactoryExplain.ClientExplainModule
    /// Modules are loaded using Managed Extensibility Framework (MEF).
    /// </summary>
    public interface ITbResultExplainer
    {
        /// <summary>
        /// Explains a addin result item
        /// </summary>
        /// <param name="subject">The subject for explainer.</param>
        /// <param name="clientServices">Service resolver.</param>
        /// <returns>Task object for the async explainer.</returns>
        Task Explain(ITbExplainData subject, ITbClientServiceLocator clientServices);
    }
}
