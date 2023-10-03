module com.example.spaceshooterusingquadtrees1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.spaceshooterusingquadtrees1 to javafx.fxml;
    exports com.example.spaceshooterusingquadtrees1;
}