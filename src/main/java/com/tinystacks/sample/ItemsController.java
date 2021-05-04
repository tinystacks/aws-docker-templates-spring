package com.tinystacks.sample;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/item")
public class ItemsController {

    private static final String TABLE_NAME = System.getenv("TABLE_NAME");


    private static CognitoIdentityProviderClient cognito = CognitoIdentityProviderClient.builder().build();

    private static DynamoDBMapper mapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().build());

    private static DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
        .withTableNameOverride(TableNameOverride.withTableNameReplacement(TABLE_NAME))
        .build();

    @DynamoDBTable(tableName="REPLACED_BY_ENV_VAR")
    public static class ResourceItem {
        private String userId;
        private String itemId;
        private String title;
        private String content;
        private String createdAt;

        @DynamoDBHashKey(attributeName="userId")
        public String getUserId() {
            return this.userId;
        }
    
        public void setUserId(String userId) {
            this.userId = userId;
        }
    
        @DynamoDBRangeKey(attributeName="itemId")
        public String getItemId() {
            return this.itemId;
        }
    
        public void setItemId(String itemId) {
            this.itemId = itemId;
        }
    
        @DynamoDBAttribute(attributeName="title")
        public String getTitle() {
            return this.title;
        }
    
        public void setTitle(String title) {
            this.title = title;
        }
    
        @DynamoDBAttribute(attributeName="content")
        public String getContent() {
            return this.content;
        }
    
        public void setContent(String content) {
            this.content = content;
        }

        @DynamoDBAttribute(attributeName="createdAt")
        public String getCreatedAt() {
            return this.createdAt;
        }
    
        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }

    @GetMapping()
    public List<ResourceItem> index(@RequestHeader(value="authorization") String authHeader) {
        return list(getUserData(authHeader));
    }

    @PutMapping()
    public void putRequest(@RequestHeader(value="authorization") String authHeader, 
      @RequestParam(value="title") String title, @RequestParam(value="content") String content) {
        // todo req id
        createItem(getUserData(authHeader), Math.random() * 99999999 + "", title, content);
    }

    @DeleteMapping
    public void deleteRequest(@RequestHeader(value="authorization") String authHeader, 
        @RequestParam(value="itemId") String itemId) {
        deleteItem(getUserData(authHeader), itemId);
    }

    // AWS helpers
    private String getUserData(String authHeader) {
        String jwtToken = authHeader.split(" ")[1];
        System.out.println(jwtToken);

        GetUserRequest request = GetUserRequest.builder().accessToken(jwtToken).build();
        GetUserResponse response = cognito.getUser(request);
        List<AttributeType> sub = response.userAttributes().stream().filter(attr -> attr.name().equals("sub")).collect(Collectors.toList());
        return sub.get(0).value();
    }

    private List<ResourceItem> list(String userId) {
        ResourceItem item = new ResourceItem();
        item.setUserId(userId);
        DynamoDBQueryExpression<ResourceItem> queryExpression = new DynamoDBQueryExpression<ResourceItem>()
            .withHashKeyValues(item);

        return mapper.query(ResourceItem.class, queryExpression, mapperConfig);
    }

    private void createItem(String userId, String itemId, String title, String content) {
        ResourceItem item = new ResourceItem();
        item.setUserId(userId);
        item.setItemId(itemId);
        item.setTitle(title);
        item.setContent(content);
        item.setCreatedAt(new Date().getTime() + "");
        mapper.save(item, mapperConfig);
    }

    private void deleteItem(String userId, String itemId) {
        ResourceItem item = new ResourceItem();
        item.setUserId(userId);
        item.setItemId(itemId);

        mapper.delete(item, mapperConfig);
    }
}