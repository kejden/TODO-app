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
    public ResponseEntity<List<TaskDto>> getTasksByCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(taskService.getTaskByCategoryId(categoryId), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskRequest task) {
        return new ResponseEntity<>(taskService.createTask(task), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskRequest task) {
        return new ResponseEntity<>(taskService.updateById(id, task), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
