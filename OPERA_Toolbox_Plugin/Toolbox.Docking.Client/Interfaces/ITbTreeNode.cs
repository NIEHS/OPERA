using System.Collections.Generic;

namespace Toolbox.Docking.Client.Interfaces
{
    /// <summary>
    /// Definition for a node in Toolbox's endpoint tree.
    /// </summary>
    public interface ITbTreeNode
    {
        /// <summary>
        /// The path to the tree node startinг from the top level e.g.:
        /// Human Health Hazards
        /// Sensitisation
        /// </summary>
        IReadOnlyList<string> Path{ get; }
    }
}