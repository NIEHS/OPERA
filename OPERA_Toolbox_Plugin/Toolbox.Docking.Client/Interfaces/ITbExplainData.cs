using Toolbox.Declarations;

namespace Toolbox.Docking.Client.Interfaces
{
    /// <summary>
    /// Data explain item definition
    /// </summary>
    public interface ITbExplainData : ITbUiState
    {
        /// <summary>
        /// Result to be explained.
        /// </summary>
        string TextToExplain{ get; }

        /// <summary>
        /// SMILES context of the result
        /// </summary>
        string Smiles { get; }
        
        /// <summary>
        /// The object that produced the explained text.
        /// </summary>
        ITbObjectId ProducerObject { get; }
    }
}