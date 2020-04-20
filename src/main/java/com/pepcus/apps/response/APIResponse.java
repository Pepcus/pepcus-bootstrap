package com.pepcus.apps.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pepcus.apps.db.entities.SearchableEntity;
import lombok.Data;

/**
 * Global response object to wrap response from all the APIs and return additional attributes.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
@JsonSerialize(using = APIResponseSerializer.class)
public class APIResponse {

  private String status;
  private String code;
  private String limit;
  private String offset;
  private String sort;
  private String totalRecords;
  private String failedRecords;
  private String successRecords;
  private String message;
  private String requestReferenceId;
  private String callbackMessage;
  private boolean result = false;

  /*
   * TODO: Replace with generic attribute like list, objects
   */
  private List list;
  private SearchableEntity searchEntity;


}
