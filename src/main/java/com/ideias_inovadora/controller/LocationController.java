package com.ideias_inovadora.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ideias_inovadora.dto.LocationDTO;
import com.ideias_inovadora.dto.LocationDTOs;
import com.ideias_inovadora.model.Location;
import com.ideias_inovadora.service.LocationService;
import com.ideias_inovadora.util.ApiResponse;
import java.util.concurrent.ConcurrentHashMap;
@RestController
@RequestMapping("api/sales-system/location/")

public class LocationController {

	@Autowired
	LocationService locationService;
	@Autowired
	ApiResponse apiResponse;
	@Autowired
    private CacheManager cacheManager;

	@PostMapping(value = "create")
	public ResponseEntity<ApiResponse> create(@Validated @RequestBody Location location) {
		apiResponse.setMessage(locationService.create(location));
		apiResponse.setStatus("Sucesso");

		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@PutMapping(value = "update")
	public ResponseEntity<ApiResponse> update(@Validated @RequestBody Location location) {
		apiResponse.setMessage(locationService.update(location));
		apiResponse.setStatus("Sucesso");
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

	}

	
	@GetMapping(value = "list")
	public ResponseEntity<List<LocationDTO>> list() throws Exception {
		return ResponseEntity.ok(this.locationService.list());

	}

	  
	    @GetMapping("/cache-info")
	    public Map<String, Object> getCacheInfo() {
	        Cache cache = cacheManager.getCache("location");
	        // Supondo que o cache utilize um ConcurrentHashMap
	        ConcurrentHashMap<?, ?> nativeCache = (ConcurrentHashMap<?, ?>) cache.getNativeCache();
	        return Map.of("cacheName", "location", "entries", nativeCache);
	    }
	@GetMapping(value = "lists")
	public ResponseEntity<List<LocationDTOs>> listCombox() throws Exception {
		return ResponseEntity.ok(this.locationService.listCompobox());

	}
	
	@DeleteMapping(value = "delete")
	public ResponseEntity<ApiResponse> delete(@RequestParam(name = "id") Long id) throws Exception {
		this.locationService.delete(id);
		apiResponse.setMessage("Deletado com Sucesso");
		apiResponse.setStatus("Sucesso");
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@GetMapping(value = "search")
	public ResponseEntity<List<LocationDTO>> search(@RequestParam(name = "nome") String nome) throws Exception {
		return ResponseEntity.ok(this.locationService.search(nome));

	}
}
