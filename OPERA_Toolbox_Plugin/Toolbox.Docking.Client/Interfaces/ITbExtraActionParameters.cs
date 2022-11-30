using Toolbox.Declarations;

namespace Toolbox.Docking.Client.Interfaces
{
    /// <summary>
    /// Definition of the UI state for the extra action input 
    /// </summary>
    public interface ITbExtraActionInputState : ITbUiState
    {
        /// <summary>
        /// ID for the Toolbox object.
        /// </summary>
        ITbObjectId ToolboxObject { get; }
    }
}