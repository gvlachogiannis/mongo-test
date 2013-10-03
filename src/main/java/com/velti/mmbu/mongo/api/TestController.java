package com.velti.mmbu.mongo.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/** @author gvlachogiannis */

@Controller
@RequestMapping("/test")
public class TestController {


    final static Logger logger= LoggerFactory.getLogger(TestController.class);

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> messageMOListener(HttpServletRequest request) {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=UTF-8");
        logger.info("Got request"+request.toString());


        String testBody="api/test was called";

        return new ResponseEntity<String>(testBody, responseHeaders, HttpStatus.OK);
    }



}
