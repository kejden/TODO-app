package io.ndk.backend.controller;

import io.ndk.backend.dto.TaskDto;
import io.ndk.backend.dto.request.TaskRequest;
import io.ndk.backend.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(Principal principal) {
        return new ResponseEntity<>(taskService.getTaskByUser(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/category-none")
    public ResponseEntity<List<TaskDto>> getTasksWithoutCategory(Principal principal) {
        return new ResponseEntity<>(taskService.getTasksWithoutCategory(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TaskDto>> getTasksByCategory(@PathVariable Long categoryId, Principal principal) {
        return new ResponseEntity<>(taskService.getTaskByCategoryId(categoryId, principal.getName()), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskRequest task, Principal principal) {
        return new ResponseEntity<>(taskService.createTask(task, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id, Principal principal) {
        return new ResponseEntity<>(taskService.getTaskById(id, principal.getName()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskRequest task, Principal principal) {
        return new ResponseEntity<>(taskService.updateById(id, task, principal.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Principal principal) {
        taskService.deleteById(id, principal.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
