import axios from "axios";
import { api } from "./constant";

export const oauth = async () => {
  try {
    const response = await axios.get(api.testLogin());
    return response.data;
  } catch (error) {
    console.error(error);
    return null;
  }
}