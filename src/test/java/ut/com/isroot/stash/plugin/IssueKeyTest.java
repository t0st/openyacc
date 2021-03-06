package ut.com.isroot.stash.plugin;

import com.isroot.stash.plugin.InvalidIssueKeyException;
import com.isroot.stash.plugin.IssueKey;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IssueKeyTest {
    @Test
    public void testConstuctor_projectKeyIsValidated() {
        assertThat(new IssueKey("ABC_123", "1").getProjectKey())
                .isEqualTo("ABC_123");

        assertThatThrownBy(() -> new IssueKey("!@#", "1"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("projectKey contains invalid characters");
    }

    @Test
    public void testParseIssueKeys() {
        final List<IssueKey> issueKeys = IssueKey.parseIssueKeys("Issue: ABC-123, CBA-321, UNDER_SCORE-123;");
        assertThat(issueKeys).containsExactly(new IssueKey("ABC", "123"),
                new IssueKey("CBA", "321"), new IssueKey("UNDER_SCORE", "123"));
    }

    @Test
    public void testParseValidIssueKey() throws InvalidIssueKeyException {
        final IssueKey parsed = new IssueKey("ABC-123");
        assertThat(parsed.getProjectKey()).isEqualTo("ABC");
        assertThat(parsed.getIssueId()).isEqualTo("123");
    }

    @Test(expected = InvalidIssueKeyException.class)
    public void testParseInvalidIssueKey() throws InvalidIssueKeyException {
        new IssueKey("invalidkey");
    }

    @Test
    public void testGetFullyQualifiedIssueKey() throws InvalidIssueKeyException {
        assertThat(new IssueKey("ABC", "123").getFullyQualifiedIssueKey()).isEqualTo("ABC-123");
        assertThat(new IssueKey("ABC-123").getFullyQualifiedIssueKey()).isEqualTo("ABC-123");
    }

    @Test
    public void testEquality() throws InvalidIssueKeyException {
        assertThat(new IssueKey("ABC-123")).isEqualTo(new IssueKey("ABC-123"));
        assertThat(new IssueKey("ABC-123").hashCode()).isEqualTo(new IssueKey("ABC-123").hashCode());

        /* Differs by issueId */
        assertThat(new IssueKey("ABC-123")).isNotEqualTo(new IssueKey("ABC-321"));
        assertThat(new IssueKey("ABC-123").hashCode()).isNotEqualTo(new IssueKey("ABC-321").hashCode());

        /* Differs by projectKey */
        assertThat(new IssueKey("ABC-123")).isNotEqualTo(new IssueKey("CBA-123"));
        assertThat(new IssueKey("ABC-123").hashCode()).isNotEqualTo(new IssueKey("CBA-123").hashCode());
    }
}