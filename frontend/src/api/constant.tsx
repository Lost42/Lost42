const API_BASE_URL = "http://localhost:8080/api";
const VERSION = "/v1";

const TEST = "/auth/login/42";

export const api = {
  testLogin: () => API_BASE_URL + VERSION + TEST,
}