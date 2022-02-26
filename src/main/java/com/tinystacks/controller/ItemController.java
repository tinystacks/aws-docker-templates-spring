package com.tinystacks.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tinystacks.exception.ResourceNotFoundException;
import com.tinystacks.model.Item;
import com.tinystacks.model.LocalItem;
import com.tinystacks.repository.ItemRepository;


@RestController
@RequestMapping("/")
public class ItemController {

	ArrayList<LocalItem> items = new ArrayList<LocalItem>();

	@Autowired
	private ItemRepository itemRepository;

	//Get one local item
	@GetMapping("/ping")
	public String getPing(){
		return "pong";
	}

	@GetMapping("/postgres-item")
	public List<Item> getAllItems() {
		return itemRepository.findAll();
	}

	@GetMapping("/postgres-item/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable(value = "id") Integer itemId)
			throws ResourceNotFoundException {
		Item item = itemRepository.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));
		return ResponseEntity.ok().body(item);
	}

	@PostMapping("/postgres-item")
	public Item createItem(@Valid @RequestBody Item item) {
		return itemRepository.save(item);
	}

	@PutMapping("/postgres-item/{id}")
	public ResponseEntity<Item> updateItem(@PathVariable(value = "id") Integer itemId,
			@Valid @RequestBody Item itemDetails) throws ResourceNotFoundException {
		Item item = itemRepository.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));

		item.setTitle(itemDetails.getTitle());
		item.setContent(itemDetails.getContent());
		final Item updatedItem = itemRepository.save(item);
		return ResponseEntity.ok(updatedItem);
	}

	@DeleteMapping("/postgres-item/{id}")
	public Map<String, Boolean> deleteItem(@PathVariable(value = "id") Integer itemId)
			throws ResourceNotFoundException {
		Item item = itemRepository.findById(itemId)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));

		itemRepository.delete(item);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}


	//Get All Local Items
	@GetMapping("/local-item")
	public ArrayList<LocalItem> getAllLocalItems() {
		return items;
	}

	//Get one local item
	@GetMapping("/local-item/{id}")
	public LocalItem getOneItem(@PathVariable(value = "id") Integer localItemId){
		return items.get(localItemId-1);
	}

	//Craete one local item
	@PostMapping("/local-item")
	public LocalItem createOneItem(@RequestBody LocalItem itemDetails) {
		LocalItem itemNew = new LocalItem(itemDetails.getId(), itemDetails.getTitle(), itemDetails.getContent());
		items.add(itemNew);
		return itemNew;
	}

	//Update one local item
	@PutMapping("/local-item/{id}")
	public LocalItem updateOneItem(@RequestBody LocalItem itemDetails) {
		LocalItem itemNew = new LocalItem(itemDetails.getId(), itemDetails.getTitle(), itemDetails.getContent());
		items.add(itemNew);
		return itemNew;
	}

	//Delete one local item
	@DeleteMapping("/local-item/{id}")
	public ArrayList<LocalItem> deleteOneItem(@PathVariable(value = "id") Integer localItemId){
		items.remove(0);
		return items;
	}
}
