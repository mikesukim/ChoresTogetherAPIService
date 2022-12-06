package chorestogetherapiservice.activity

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class ChoresActivitySpec extends Specification{

    @Subject
    @Shared
    ChoresActivity choresActivity = new ChoresActivity()

    def 'test getChore'() {
        when:
        def result = choresActivity.getChore()

        then:
        result == 'hello chore'
    }

    def 'test createChore'() {
        when:
        def result = choresActivity.createChore()

        then:
        result == 'hello chore'
    }
}
