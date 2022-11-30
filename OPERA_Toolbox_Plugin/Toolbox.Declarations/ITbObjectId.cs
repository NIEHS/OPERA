using System;

namespace Toolbox.Declarations
{
    /// <summary>
    /// Definition for the Toolbox object identifier.
    /// </summary>
    public interface ITbObjectId
    {
        /// <summary>
        /// Caption of the object. This will be used for visualization purposes when displaying 
        /// the toolbox object within the Toolbox client UI.
        /// </summary>
        string Caption { get; }

        /// <summary> 
        /// Object unique identifier. Should persist between application runs.
        /// </summary>
        Guid Guid { get; }

        /// <summary>
        /// Object version
        /// </summary>
        Version Version { get; }
    }
}