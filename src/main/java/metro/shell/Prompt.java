package metro.shell;

import metro.repository.MetroRepository;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public record Prompt(MetroRepository repository) implements PromptProvider {
    @Override
    public AttributedString getPrompt() {
        return new AttributedString(repository().getMetroName() + ":>",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }
}
