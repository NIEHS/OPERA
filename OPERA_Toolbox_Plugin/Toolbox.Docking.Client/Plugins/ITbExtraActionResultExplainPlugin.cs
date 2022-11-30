using System.Threading.Tasks;
using Toolbox.Declarations;
using Toolbox.Docking.Client.Interfaces;
using Toolbox.Docking.Client.Services;

namespace Toolbox.Docking.Client.Plugins
{
    /// <summary>
    /// Client side explainer for third party addin results.
    /// </summary>
    public interface ITbExtraActionResultExplainPlugin
    {
        /// <summary>
        /// Method that explains the third party addin result.
        /// </summary>
        /// <param name="clientServices">Toolbox services provider.</param>
        /// <param name="input">Current state of the Client UI</param>
        /// <param name="toExplain">The item that will be explained by the client side explainer.</param>
        /// <returns>Async C# Task for explainer.</returns>
        Task ExplainExtraActionResult(ITbClientServiceLocator clientServices, ITbExtraActionInputState input, ITbExtraActionResult toExplain);
    }
}