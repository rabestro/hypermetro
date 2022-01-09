package metro.controller

import spock.lang.Issue
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Title

@Title("Command's parameters parser")
@Narrative("""
if the name of the line or station consists of several words, 
you should write it in quotation marks (after, they should be 
excluded from the name). If the name of a station consists 
of one word it should be parseable with or without quotes.
""")
@Issue("https://hyperskill.org/projects/120/stages/649/implement")
class ParameterParserSpec extends Specification {

    def "should parse parameters"() {
        given: 'the parameter parser'
        def parser = new ParameterParser()

        when: 'we parse line with parameters'
        def parameters = parser.parse(line)

        then: 'we got expected list of parameters'
        parameters == expected

        where:
        line                                   | expected
        ''                                     | []
        '      '                               | []
        '     ""    '                          | []
        null                                   | []
        'Waterloo'                             | ['Waterloo']
        '"Waterloo"'                           | ['Waterloo']
        '     "Waterloo"'                      | ['Waterloo']
        '     Waterloo'                        | ['Waterloo']
        '     Waterloo    '                    | ['Waterloo']
        'Bakerloo Waterloo'                    | ['Bakerloo', 'Waterloo']
        '"Bakerloo" "Waterloo"'                | ['Bakerloo', 'Waterloo']
        'Bakerloo "Waterloo"'                  | ['Bakerloo', 'Waterloo']
        '"Bakerloo" Waterloo'                  | ['Bakerloo', 'Waterloo']
        'Bakerloo     Waterloo'                | ['Bakerloo', 'Waterloo']
        '"Hammersmith & City"'                 | ['Hammersmith & City']
        '"Hammersmith & City" "Goldhawk Road"' | ['Hammersmith & City', 'Goldhawk Road']
        'Circle Temple Central Leyton'         | ['Circle', 'Temple', 'Central', 'Leyton']
        'Circle Temple Central "Gants Hill"'   | ['Circle', 'Temple', 'Central', 'Gants Hill']
    }
}
