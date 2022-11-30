namespace Toolbox.Declarations
{
    /// <summary>
    /// The Toolbox server mode: single or multi user
    /// </summary>
    public enum TbServerMode
    {
        /// <summary>
        /// user can create/edit TB objects (for example: profilers, QSARs)
        /// </summary>
        SingleUser,
        
        /// <summary>
        /// New Toolbox objects cannot be created and existing cannot be modified 
        /// </summary>
        MultiUser
    }
}
