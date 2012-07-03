package teammates.datatransfer;

import static teammates.api.Common.EOL;
import teammates.api.Common;
import teammates.api.InvalidParametersException;
import teammates.storage.entity.Student;

import com.google.appengine.api.datastore.Text;

public class StudentData extends UserData {
	public enum UpdateStatus {
		ERROR(0), NEW(1), MODIFIED(2), UNMODIFIED(3), NOT_IN_ENROLL_LIST(4), UNKNOWN(
				5);

		public final int numericRepresentation;

		private UpdateStatus(int numericRepresentation) {
			this.numericRepresentation = numericRepresentation;
		}

		public static UpdateStatus enumRepresentation(int numericRepresentation) {
			switch (numericRepresentation) {
			case 0:
				return ERROR;
			case 1:
				return NEW;
			case 2:
				return MODIFIED;
			case 3:
				return UNMODIFIED;
			case 4:
				return NOT_IN_ENROLL_LIST;
			case 5:
				return UNKNOWN;
			default:
				return UNKNOWN;
			}
		}
	}

	public String name;
	public String email;
	public String course = null;
	public String comments = null;
	public String team = null;
	public Text profile = null;
	public String key = null;

	public UpdateStatus updateStatus = UpdateStatus.UNKNOWN;

	public transient EvalResultData result;

	public StudentData(String email, String name, String comments,
			String courseId, String team) {
		this();
		this.email = email;
		this.course = courseId;
		this.name = name;
		this.comments = comments;
		this.team = team;
	}

	public StudentData() {
		isStudent = true;
	}

	public StudentData(String enrollLine, String courseId)
			throws InvalidParametersException {
		this(new Student(enrollLine, courseId));
	}

	public StudentData(Student student) {
		this();
		this.email = student.getEmail();
		this.course = student.getCourseID();
		this.name = student.getName();
		this.comments = student.getComments();
		this.team = student.getTeamName();
		this.profile = student.getProfileDetail();
		this.id = student.getID();
		Long keyAsLong = student.getRegistrationKey();
		this.key = (keyAsLong == null ? null : Student
				.getStringKeyForLongKey(keyAsLong));
	}

	public boolean isEnrollInfoSameAs(StudentData otherStudent) {
		return (otherStudent != null) && otherStudent.email.equals(this.email)
				&& otherStudent.course.equals(this.course)
				&& otherStudent.name.equals(this.name)
				&& otherStudent.comments.equals(this.comments)
				&& otherStudent.team.equals(this.team);
	}

	public boolean isEnrollmentInfoMatchingTo(StudentData other) {
		return (this.email.equals(other.email))
				&& (this.course.equals(other.course))
				&& (this.name.equals(other.name))
				&& (this.comments.equals(other.comments))
				&& (this.team.equals(other.team))
				&& (this.updateStatus == other.updateStatus);
	}

	public String toString() {
		return toString(0);
	}

	public String toString(int indent) {
		String indentString = Common.getIndent(indent);
		StringBuilder sb = new StringBuilder();
		sb.append(indentString + "Student:" + name + "[" + email + "]" + EOL);
		sb.append(result.toString(indent + 2));
		return sb.toString();
	}

	public static void equalizeIrrelevantData(StudentData expectedStudent,
			StudentData actualStudent) {
		// For these fields, we consider null and "" equivalent.
		if ((expectedStudent.id == null) && (actualStudent.id.equals(""))) {
			actualStudent.id = null;
		}
		if ((expectedStudent.team == null) && (actualStudent.team.equals(""))) {
			actualStudent.team = null;
		}
		if ((expectedStudent.comments == null)
				&& (actualStudent.comments.equals(""))) {
			actualStudent.comments = null;
		}

		// prentend keys match because the key is generated on the server side
		// and cannot be anticipated
		if ((actualStudent.key != null)) {
			expectedStudent.key = actualStudent.key;
		}

	}

}
