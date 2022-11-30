using System;

namespace Toolbox.Docking.Client.Interfaces
{
    /// <summary>
    /// Toolbox chemical 
    /// </summary>
    public interface ITbChemical
    {
        /// <summary>
        /// Persisting identifier for the chemical.
        /// </summary>
        Guid Id { get; }

        /// <summary>
        /// SMILES representation for the structure.
        /// </summary>
        string Smiles { get; }


        /// <summary>
        /// Chemical Abstract Number for the structure.
        /// </summary>
        int CasNo { get; }
    }
}