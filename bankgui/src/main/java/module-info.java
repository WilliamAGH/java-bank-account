/**
 * This module-info.java file declares the dependencies for the BankGUI application
 * It includes JavaFX modules, Ikonli for icons, and AtlantaFX for themes
 * @author William Callahan
 * @since April 29, 2025
 */
module com.example.bankgui {
requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires atlantafx.base;

    opens com.example.bankgui to javafx.fxml, javafx.base;

    exports com.example.bankgui;
}