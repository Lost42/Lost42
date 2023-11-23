import { oauth } from "../api/api";

export default function MainPage() {
  const handleOAuth = async () => {
    oauth();
  }
  return (
    <div>
      <h1>MainPage</h1>
      <button onClick={handleOAuth}>OAuth</button>
    </div>
  );
}