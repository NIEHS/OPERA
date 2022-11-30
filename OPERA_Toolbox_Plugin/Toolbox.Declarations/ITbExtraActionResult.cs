namespace Toolbox.Declarations
{
    /// <summary>
    /// Definition of a result produced by the extra action sub-system
    /// </summary>
    public interface ITbExtraActionResult
    {
        /// <summary>
        ///     Name of client's plugin (ITbExtraActionResultExplainPlugin) which can explain this result
        /// </summary>
        string Explainer { get; }

        /// <summary>
        ///     integer result
        /// </summary>
        int Result { get; }

        /// <summary>
        ///     string encoded result.
        /// </summary>
        string Payload { get; }
    }
}