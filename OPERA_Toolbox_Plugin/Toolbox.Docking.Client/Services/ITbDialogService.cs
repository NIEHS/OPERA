using System.Collections.Generic;
using System.Windows;

namespace Toolbox.Docking.Client.Services
{
    /// <summary>
    /// UI services provider.
    /// </summary>
    public interface ITbDialogService
    {
      
         /// <summary>
         ///   Displays a non-modal window.
         ///   For any window shown by this method, the following is guaranteed to be true:
         ///    1. The Shell (MainWindow) will take ownership of the window
         ///    2. The dialog will be positioned at the center of it's Owner (The Shell)          
         /// </summary>
         /// <param name="window">The window to be displayed.</param>
         /// <param name="location">Preferred startup location</param>
        void Show(Window window, WindowStartupLocation location = WindowStartupLocation.CenterOwner);

         /// <summary>
         ///  Displays a modal window.
         ///  For any dialog shown by this method, the following is guaranteed to be true:
         ///    1. The Shell (MainWindow) will take ownership of the dialog
         ///    2. The dialog will be positioned at the center of it's Owner (The Shell)
         /// </summary>
         /// <param name="dialog">The window to be displayed.</param>
         /// <param name="location">Preferred startup location</param>
         /// <returns>Return the result of dialog.ShowDialog</returns>
        bool ShowDialog(Window dialog, WindowStartupLocation location = WindowStartupLocation.CenterOwner);

        /// <summary>
        /// Displays a Request confirmation dialog to the user
        /// </summary>
        /// <param name="message">The question to be asked</param>
        /// <param name="title">Title caption for the dialog.</param>
        /// <param name="skippable">Will skip dialog option be provided.</param>
        /// <param name="messageAlignment">TextAlignment for the message.</param>
        /// <param name="width">Width for the dialog.</param>
        /// <param name="height">Height for the dialog.</param>
        /// <returns>True, if the user agree</returns>
        bool Confirm(string message, string title = null, bool skippable = false, TextAlignment messageAlignment = TextAlignment.Center, int width = 550, int height = 200);

        /// <summary>
        /// Use this instead MessageBox.Show(), which happens to be non-modal for unknown reasons
        /// </summary>
        /// <param name="msg">Text for the displayed message.</param>
        /// <param name="title">Title caption for the dialog.</param>
        /// <param name="additionalInfo">Key value pairs which will be rendered as additional message information</param>
        /// <param name="msgAlignment">Shows how to align the message in the dialog</param>
        void ShowMessage(string msg, string title, IEnumerable<KeyValuePair<string, string>> additionalInfo = null, TextAlignment msgAlignment = TextAlignment.Center);

    }
}