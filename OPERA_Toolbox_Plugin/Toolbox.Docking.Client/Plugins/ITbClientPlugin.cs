using System.Threading.Tasks;
using Toolbox.Docking.Client.Interfaces;
using Toolbox.Docking.Client.Services;

namespace Toolbox.Docking.Client.Plugins
{
    /// <summary>
    /// Plugin module which will be activated when an extra action is activated in case TbExtraActionDefinition.ClientModule is assigned.
    /// </summary>
    public interface ITbClientPlugin
    {
        /// <summary>
        /// Execute client side plugin.
        /// </summary>
        /// <param name="clientServices">Service locator object.</param>
        /// <param name="input">UI input state</param>
        /// <returns>Returns C# Task object for async support.</returns>
        Task ClientPluginExecute(ITbClientServiceLocator clientServices, ITbExtraActionInputState input);
    }
}
