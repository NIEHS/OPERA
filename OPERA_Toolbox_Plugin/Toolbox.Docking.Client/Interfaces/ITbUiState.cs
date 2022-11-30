namespace Toolbox.Docking.Client.Interfaces
{

    /// <summary>
    /// UI state provider object.
    /// </summary>
    public interface ITbUiState
    {
        /// <summary>
        /// Toolbox Client information.
        /// </summary>
        ITbClient TbClient { get; }
        /// <summary>
        /// Currently selected document.
        /// </summary>
        ITbDocument CurrentDocument{ get; }

        /// <summary>
        /// Currently selected list.
        /// </summary>
        ITbChemList CurrentList { get; }

        /// <summary>
        /// Currently selected chemical.
        /// </summary>
        ITbChemical CurrentChemical { get; }
        /// <summary>
        /// Current document's selected endpoint tree.
        /// </summary>
        ITbTreeNode EndpointTree{ get; }
    }
}