package com.pepcus.apps.response;

import java.io.IOException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pepcus.apps.db.entities.SearchableEntity;
import com.pepcus.apps.exception.APIErrorCodes;
import com.pepcus.apps.exception.ApplicationException;

/**
 * Custom serializer to handle json attribute name for different data
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public class APIResponseSerializer extends JsonSerializer<APIResponse> {

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
   * com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
   */
  @Override
  public void serialize(APIResponse apiResponse, JsonGenerator jGen, SerializerProvider serializerProvider) {
    try {

      jGen.writeStartObject();

      // To check if the response is required just for checking health of JAPI application
      if (apiResponse.isResult()) {
        jGen.writeBooleanField("result", apiResponse.isResult());
        // Else a normal API call for any JAPI resource
      } else {

        serializeRequestReferenceId(apiResponse, jGen);

        serializeResponseStatus(apiResponse, jGen);

        serializeListData(apiResponse, jGen);

        serializeSearchEntity(apiResponse, jGen);

        serializeMessage(apiResponse, jGen);
      }

      jGen.writeEndObject();
    } catch (Exception ex) {
      throw ApplicationException.createInternalError(APIErrorCodes.ERROR_WRITING_JSON_OUTPUT, apiResponse.toString());
    }
  }

  /**
   * To serialize requestReferenceId
   * 
   * @param apiResponse
   * @param jGen
   * @throws IOException
   */
  private void serializeRequestReferenceId(APIResponse apiResponse, JsonGenerator jGen) throws IOException {
    jGen.writeStringField("requestReferenceId", apiResponse.getRequestReferenceId());
  }

  /**
   * To serialize message
   * 
   * @param apiResponse
   * @param jGen
   * @throws IOException
   */
  private void serializeMessage(APIResponse apiResponse, JsonGenerator jGen) throws IOException {
    if (StringUtils.isNotBlank(apiResponse.getMessage())) {
      jGen.writeStringField("message", apiResponse.getMessage());
    }
  }

  /**
   * To serialize response status
   * 
   * @param apiResponse
   * @param jGen
   * @throws IOException
   */
  private void serializeResponseStatus(APIResponse apiResponse, JsonGenerator jGen) throws IOException {
    jGen.writeStringField("status", apiResponse.getStatus());
    jGen.writeStringField("code", apiResponse.getCode());
    if (!StringUtils.isEmpty(apiResponse.getCallbackMessage())) {
      jGen.writeStringField("callbackMessage", apiResponse.getCallbackMessage());
    }
  }

  /**
   * To serialize searchable entity
   * 
   * @param apiResponse
   * @param jGen
   * @throws IOException
   */
  private void serializeSearchEntity(APIResponse apiResponse, JsonGenerator jGen) throws IOException {
    if (apiResponse.getSearchEntity() != null) {
      jGen.writeFieldName(apiResponse.getSearchEntity().getNodeName());
      jGen.writeObject(apiResponse.getSearchEntity());
    }
  }

  /**
   * To serialize searchable list data
   * 
   * @param apiResponse
   * @param jGen
   * @throws IOException
   */
  private void serializeListData(APIResponse apiResponse, JsonGenerator jGen) throws IOException {
    if (apiResponse.getList() != null) {
      List list = apiResponse.getList();
      Object object = list.get(0);
      if (object instanceof SearchableEntity) {
        SearchableEntity searchEntity = (SearchableEntity) object;
        jGen.writeStringField("limit", apiResponse.getLimit());
        jGen.writeStringField("offset", apiResponse.getOffset());
        String sort = apiResponse.getSort();
        jGen.writeStringField("sort", sort);
        jGen.writeStringField("totalRecords", apiResponse.getTotalRecords());

        jGen.writeFieldName(searchEntity.getMultiDataNodeName());
        jGen.writeStartArray();
        for (Object obj : list) {
          jGen.writeObject(obj);
        }
        jGen.writeEndArray();
      }
    }
  }
}
