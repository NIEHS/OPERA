namespace Toolbox.Docking.Client.Interfaces
{
    /// <summary>
    /// Definition for a Toolbox list
    /// </summary>
    public interface ITbChemList
    {
        /// <summary>
        /// Chemical list caption, to be used for UI purposes.
        /// </summary>
        string ListCaption { get; }
        /// <summary>
        /// The name of the task currently operation on the chemical list
        /// </summary>
        string RunningTaskName { get; }

        /// <summary>
        /// Target for chemical list.
        /// </summary>
        ITbChemical TargetChemical { get; }
    }
}