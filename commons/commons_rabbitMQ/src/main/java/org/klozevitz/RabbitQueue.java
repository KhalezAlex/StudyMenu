package org.klozevitz;

/**
 * Подумать над тем, кто будет грузить документы - кажется, что это должны делать отделения, а не компания
 * */

public class RabbitQueue {
    public static final String COMPANY_TEXT_UPDATE = "company_text_update";
    public static final String COMPANY_COMMAND_UPDATE = "company_command_update";
    public static final String COMPANY_CALLBACK_QUERY_UPDATE = "company_callback_query_update";
    public static final String COMPANY_ANSWER_MESSAGE = "company_answer_message";

    public static final String DEPARTMENT_TEXT_UPDATE = "department_text_update";
    public static final String DEPARTMENT_COMMAND_UPDATE = "department_command_update";
    public static final String DEPARTMENT_CALLBACK_QUERY_UPDATE = "department_callback_query_update";
    public static final String DEPARTMENT_DOC_UPDATE = "department_doc_update";
    public static final String DEPARTMENT_ANSWER_MESSAGE = "department_answer_message";

    public static final String EMPLOYEE_TEXT_UPDATE = "employee_text_update";
    public static final String EMPLOYEE_COMMAND_UPDATE = "employee_command_update";
    public static final String EMPLOYEE_CALLBACK_QUERY_UPDATE = "employee_callback_query_update";
    public static final String EMPLOYEE_ANSWER_MESSAGE = "employee_answer_message";
}
