package com.example.controller;

import com.example.model.Blog;
import com.example.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private IBlogService blogService;

    @GetMapping("")
    public String index(Model model) {
        List<Blog> blogList = blogService.findAll();
        model.addAttribute("blogs", blogList);
        return "index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("blog", new Blog());
        return "create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Blog blog, RedirectAttributes redirectAttributes) {
        try {
            if (blog.getId() == null) {
                blog.setCreatedAt(LocalDateTime.now());
                blog.setUpdatedAt(LocalDateTime.now());
            } else {
                Blog existingBlog = blogService.findById(blog.getId());
                blog.setCreatedAt(existingBlog.getCreatedAt());
                blog.setUpdatedAt(LocalDateTime.now());
            }
            blogService.save(blog);
            redirectAttributes.addFlashAttribute("success", "Blog saved successfully!");
            return "redirect:/blog";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving blog: " + e.getMessage());
            return "redirect:/blog/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.findById(id));
        return "edit";
    }

    @PostMapping("/update")
    public String update(Blog blog) {
        Blog existingBlog = blogService.findById(blog.getId());
        blog.setCreatedAt(existingBlog.getCreatedAt());
        blog.setUpdatedAt(LocalDateTime.now());
        blogService.save(blog);
        return "redirect:/blog";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        blogService.remove(id);
        redirect.addFlashAttribute("success", "Xóa blog thành công!");
        return "redirect:/blog";
    }

//    @PostMapping("/delete")
//    public String delete(@RequestParam("id") Long id, RedirectAttributes redirect) {
//        blogService.remove(id);
//       redirect.addFlashAttribute("success", "Xóa blog thành công!");
//       return "redirect:/blog";
//   }

    @GetMapping("/{id}/view")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.findById(id));
        return "view";
    }
}
