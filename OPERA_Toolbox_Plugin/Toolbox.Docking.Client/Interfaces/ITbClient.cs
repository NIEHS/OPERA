using System;
using Toolbox.Declarations;

namespace Toolbox.Docking.Client.Interfaces
{
    /// <summary>
    /// Toolbox client information
    /// </summary>
    public interface ITbClient
    {
        /// <summary>
        /// Mode of the Server the Client is connected to.
        /// </summary>
        TbServerMode ServerMode { get; }

        /// <summary>
        /// Session ID of the Client-Server connection
        /// </summary>
        Guid SessionId { get; }

        /// <summary>
        /// Client's ID
        /// </summary>
        Guid ClientId { get; }
    }
}