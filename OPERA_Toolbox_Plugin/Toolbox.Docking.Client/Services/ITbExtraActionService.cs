using System;
using System.Threading.Tasks;
using Toolbox.Declarations;
using Toolbox.Docking.Client.Interfaces;

namespace Toolbox.Docking.Client.Services
{
    /// <summary>
    /// Provides chanel a client module to communicate with server party
    /// </summary>
    public interface ITbExtraActionService
    {
        /// <summary>
        /// Method for executing addin extra actions.
        /// </summary>
        /// <param name="uiState">Current state of the Client UI.</param>
        /// <param name="actionCaption">The UI presentable name of the action being executed.</param>
        /// <param name="tbObjectId">The identifier of the executing Toolbox object.</param>
        /// <param name="actionId">The ID of the executed action.</param>
        /// <param name="command">Command variant.</param>
        /// <param name="payload">String encoded payload for the action.</param>
        /// <returns>Returns <see cref="ITbExtraActionResult">source template</see> wrapped as a C# Task for async support.</returns>
        Task<ITbExtraActionResult> ExecuteExtraAction(ITbUiState uiState, string actionCaption, Guid tbObjectId, Guid actionId, int command, string payload);
    }
}