module conta.desktop {
    // usa javafx
    requires javafx.controls;
    // usa spring
    requires spring.web;

    // abre telas e builds
    opens conta.tela;
    opens conta.to;
}