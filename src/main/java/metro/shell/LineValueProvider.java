package metro.shell;

import metro.repository.MetroRepository;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public record LineValueProvider(MetroRepository repository) implements ValueProvider {

    @Override
    public boolean supports(MethodParameter parameter, CompletionContext completionContext) {
        return parameter.getParameterType().isAssignableFrom(String.class)
                && parameter.getParameterIndex() == completionContext.getWordIndex();
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        var currentInput = completionContext.currentWordUpToCursor();

        return repository().getSchema().keySet().stream()
                .filter(line -> line.startsWith(currentInput))
                .map(CompletionProposal::new)
                .toList();
    }
}
