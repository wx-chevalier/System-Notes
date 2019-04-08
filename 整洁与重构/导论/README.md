# 软件工程中的问题

Generally speaking, there are 3. The Performant System Problem, the Embedded System Problem, and the Complex Domain Problem.

##  The Performant System Problem

Let's talk about Twitter.

Twitter is actually a really simple concept.

You sign up, you make tweets, you like other people's tweets and that's pretty much it.

If Twitter is that simple, why couldn't someone else do it?

It's apparent that the real challenge for Twitter is not actually so much as "what it does", but it's "how it's able to do what it does".

Twitter has the unique challenge of serving requests from approximately 500 million users every single day.

The hard problem that Twitter solves is actually a perfomance problem.

When the challenge is performance, whether we use a strictly typed language or not is less important.

## The Embedded System Problem

An embedded system is a combination of computer hardware and software, with the purpose of enabling control over the mechanical or electrical aspects of a system.

Most systems we use today are built on a very complex layer of code that, if not initially written in, compiles down to C or C++ usually.

Coding in these languages is not for the faint of heart.

In C, there is no such thing as objects; and we as humans like objects because we can easily understand them. C is procedural and this makes the code that we have to write in this language more challenging to keep clean. These problems also require knowledge of the lower-level details.

C++ does make life a whole lot better because it has object orientation, but the challenge is still fundementally interacting with lower-level hardware details.

Because we don't really have that much of a choice on the languages we use for these problems, it's irrelevant to discuss TypeScript here.

## The Complex Domain Problem

For some problems, that challenge is less about scaling in terms of handling more requests, but scaling in terms of the codebase's size.

Enterprise companies have complex real-life problems to be solved. In these companies, the biggest engineering challenges are usually:

- Being able to logically (via domains) separate parts of that monolith into smaller apps. And then, physically (via microservices for bounded contexts) split them up so that teams can be assigned to maintain them

- Handling integration and synchronization between these apps

- Modeling the domain concepts and actually solving the problems of the domain

- Creating a ubiquitous (all encompassing) language to be shared by developers and domain experts

- Not getting lost in the mass amounts of code written and slowing down to the point where it becomes impossible to add new features without breaking existing ones

I've essentially described the types of problems that Domain Driven Design solves. For these types of projects, you wouldn't even think about not using a strictly-typed language like TypeScript.