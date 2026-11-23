package baseNoStates.requests;

import org.json.JSONObject;

/**
 * Common interface for all request types in the access control system.
 * Each request can be processed and converted to a JSON answer.
 */
public interface Request {

  /**
   * Returns the answer of this request as a JSON object.
   *
   * @return the JSON answer
   */
  JSONObject answerToJson();

  @Override
  String toString();

  /**
   * Processes the request.
   */
  void process();
}