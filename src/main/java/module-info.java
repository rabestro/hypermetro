module hypermetro.main {
    exports metro;
    opens metro.config to spring.core, spring.beans, spring.context;

    requires spring.context;
    requires spring.core;
    requires com.google.gson;
    requires static lombok;
}