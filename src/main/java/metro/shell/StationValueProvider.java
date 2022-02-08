package metro.shell;

import metro.model.Station;
import metro.repository.MetroRepository;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.List;
import java.util.stream.Stream;

@Component
public record StationValueProvider(MetroRepository repository) implements ValueProvider {
    @Override
    public boolean supports(MethodParameter parameter, CompletionContext completionContext) {
        return parameter.getParameterType().isAssignableFrom(String.class)
                && parameter.getParameterIndex() % 2 == 1
                && parameter.getParameterIndex() == completionContext.getWordIndex();
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        var currentInput = completionContext.currentWordUpToCursor();
        var metroLine = completionContext.getWords().get(parameter.getParameterIndex() - 1);
        return Stream.ofNullable(repository().findLine(metroLine))
                .flatMap(Deque::stream)
                .map(Station::name)
                .filter(name -> name.startsWith(currentInput))
                .map(CompletionProposal::new)
                .toList();

    }
}
