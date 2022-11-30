namespace Toolbox.Docking.Client.Services
{
    /// <summary>
    /// Service injection, service location
    /// </summary>
    public interface ITbClientServiceLocator
    {
        /// <summary>
        /// The DialogService provides various UI services.
        /// </summary>
        ITbDialogService DialogService { get; }
        /// <summary>
        /// Service for executing an extra Toolbox action.
        /// </summary>
        ITbExtraActionService ExtraActionService { get; }
    }
}