package com.example.webblog.controllers;

import com.example.webblog.entities.Post;
import com.example.webblog.repositories.PostRepository;
import com.example.webblog.repositories.UserRepository;
import com.example.webblog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(value = "/blog")
public class BlogController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String blog(Model model) {
        Iterable<Post> posts = postRepository.findPostsByUserId(userRepository.findByUsername(userService.getCurrentUsername())
                .getId());
        model.addAttribute("posts", posts);
        return "blog";
    }

    @GetMapping("/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = new Post(userRepository.findByUsername(userService.getCurrentUsername())
                .getId(), title, anons, full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }


    @GetMapping("/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> postText = postRepository.findById(id);
        List<Post> postList = new ArrayList<>();
        postText.ifPresent(postList::add);
        if (!Objects.equals(postList.get(0).getUserId(), userRepository.findByUsername(userService.getCurrentUsername())
                .getId())) {
            return "redirect:/blog";
        }
        model.addAttribute("post", postList);
        return "blog-details";
    }

    @GetMapping("/{id}/view")
    public String blogView(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/";
        }
        Optional<Post> postText = postRepository.findById(id);
        List<Post> post = new ArrayList<>();
        postText.ifPresent(post::add);
        model.addAttribute("post", post);
        return "blog-view";
    }

    @GetMapping("/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> postText = postRepository.findById(id);
        List<Post> postList = new ArrayList<>();
        postText.ifPresent(postList::add);
        if (!Objects.equals(postList.get(0).getUserId(), userRepository.findByUsername(userService.getCurrentUsername())
                .getId())) {
            return "redirect:/blog";
        }
        model.addAttribute("post", postList);
        return "blog-edit";
    }

    @PostMapping("/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/{id}/delete")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {

        Post post = postRepository.findById(id).orElseThrow();
        if (!Objects.equals(post.getUserId(), userRepository.findByUsername(userService.getCurrentUsername())
                .getId())) {
            return "redirect:/blog";
        }
        postRepository.delete(post);
        return "redirect:/blog";
    }
}
