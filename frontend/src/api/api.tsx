import axios from "axios";
import { api } from "./constant";

export const oauth = async () => {
  try {
    const response = await axios.get(api.testLogin());
    console.log(response);
    return response.data;
  } catch (error) {
    console.log(error);
    return null;
  }
}