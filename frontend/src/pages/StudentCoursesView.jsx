// This is the Student Courses View page
// It allows students to view the courses they are enrolled in.
export default function StudentCoursesView() {
  return (
    <div style={styles.container}>
      <h1>My Courses</h1>
      <ul>
        <li>Course 1</li>
        <li>Course 2</li>
        <li>Course 3</li>
      </ul>
    </div>
  );
}

const styles = {
  container: { padding: "2rem" },
};



