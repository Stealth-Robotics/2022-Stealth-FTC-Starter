# Experiments w/ different ways to set us up for FTC next season

This repo is to help us figure out how we want to do FTC development next season.

**THIS REPO IS TEMPORARY.**

# Goals

- Provide an easy start w/ minimal big concepts
- Set them up for success w/ a good library which promotes correctness (FTCLib or similar)
- Avoid a single, massive opmode loop w/ nested loops that get them into trouble
    - FSM's aren't approachable
    - Monolothic files make it hard for multiple people to work different parts of the bot in
      parallel
    - Far too easy to confuse controller binding
    - Too easy to break a working part while adding something new
    - Too hard to make sure operations don't interfere w/ each other
- Encourage minimal duplicated code
- Good starter samples that fit into this model
- Easy migration to RoadRunner and OpenCV
- All FTC programmers grow naturally into FRC programmers

## Repo for next season

I imagine we have a repo very much like this one, where each team dir is a copy of `StarterCode`
with the two-wheeled bot code, and branches with further examples in `StarterCode` for mecanum,
RoadRunner, OpenCV, more interesting command and subsystem examples, etc.

# Current Approach

- FTCLib: commands & subsystems set them up for success w/ no nested while loops, obvious factoring
  into multiple files, and super-easy composition for tele-op and auto.
    - Really easy controller binding
    - Lots of options for how to run/combine commands in response to input or during autos.
    - Purposeful port of WPILib, so great parity. FTC students looking at FRC code the first time
      should feel at home.
    - The Command model has decent overlap with modern game programming (e.g., Unity) which will be
      a good analogy for some.
- Monorepo
    - All team projects are in the same repo, and under the same Andriod Studio project.
    - Each team edits it's own code and commits/pulls as usual.
    - All laptops are setup the same way; it doesn't matter which laptop a team uses night-to-night.
- StealthLib
    - I have a very rough start at a common library for the team where we can place common, useful
      things for all teams to use. Ideally this grows and helps us be more effective each season.
    - More advanced students can contribute to the library and help each team make the best use of
      it.

# In-progress work

## StarterCode

I have the `StarterCode` project in, I think, reasonable shape. I'd recommend we start them with
just the
`SimpleTwoWheelDriveSubsystem` and associated commands. This is the exact code from the normal
example we start with for the two-wheeled bot we build the first day, just broken up into the
teleop, command, and subsystem.

I have some basic mecanum code in there, in `SimpleMecanumDriveSubsystem` and it's command, which is
the gm0 sample. I'd suggest we put this in a separate branch and show them when they build their
first bot w/ mecanum.

We can have further branches which show more advanced things in `StarterCode`, like RoadRunner and
OpenCV.

## MishapCode

I have ported over all the Mishap code from https://github.com/TristenYim/FtcRobotController. I
tried to keep most of the structure, comments, and code just moved around into commands and
subsystems. I think I did okay, and I'd love to have others review/compare to see what they think!

## StealthLib

I have put a few things in here that were useful in either `MishapCode` or in `MikeBotCode`. I
expect this to continue to evolve as we do more.

## Other dirs

I would love to see a few people (Samarth? Clem?) port over the Autonomous Prime code and the
Jankbot code. Those directories are just stubs right now, with some random in-progress version of
StarterCode.

- Mishap repo: https://github.com/TristenYim/FtcRobotController
- Auto Prime repo: https://github.com/clemstardust/AutoPrime2021
- Jankbot: https://github.com/SamarthRao91/FreightFrenzy2021-Jankbot13648
- FRC: https://github.com/Stealth-Robotics/Stealth2022

# TODO

These are a bunch of raw notes for in-progress work.

- more examples, in branches
- turn off that annoying code analysis and todo analysis before comitting.
    - It's in the git settings, on commit. Can turn on auto-formatting, too.
    - Can we make that config easy to carry to the team laptops?
- dashboard?
    - What's in there now, what should we be using?
    - guidance on where to println or log. API's? Where to see (logcat vs. run output)
    - guidance on prints/logs vs. telemetry/dashboard
- guidance on subsystems, commands, periodic vs default cmds,
    - Subsystems: model of the hardware, expose very simple methods to act on the HW, operates in
      the HW's terms (encoder ticks, power, etc.)
    - Commands: provide reasonable, composable actions on one or more subsystems in non-HW terms.
      Move arm between 0% and 100%, not -3000 ticks and +5000 ticks.
    - If we change the gear ratio on a motor, only a subsystem and at most the commands acting on it
      need to change. Having to change uses of commands in a case like this is bad; you have to
      basically look across the whole project and make changes, which is error-prone.
- good examples of auto movement
    - roadrunner, yes
    - but also simple things, like drive fwd for a distance or a few seconds, turn 90 degrees. The
      simple stuff Mishap and AP started with.
        - We can literally turn some of the ported Mishap commands into good examples here.
- hardware caching
    - go manual and embed it in our loops?
    - mmm: I saw every team get lucky on this multiple times. Other libs are more explicit about
      this. Watch for RR: their starter code changes the caching mode unilaterally!!
- useful utilities either in FTCLib, or that need to be in SealthLib. We need to be promoting some
  of these concepts more aggressively with the students, both FTC and FRC.
    - debounce
    - deadbands
    - what others? (Check the FRC notes and suggestions from Sidd about filters and such.)
- can we make this work well w/ on-bot?
    - Could make the lib and examples work, but for the monorepo we'd need AS I think.
    - mmm: I have a lot of issues w/ on-bot after just one season.
        - lost code many, many times. Two teams, random loss, often multiple per night. Really
          rough.
            - Jim saw teams at worlds wrecked by this multiple times.
        - Intellisense is woeful. Students learn best with intellisense hints w/ embeded javadocs,
          and having them missing really hurts.
            - Hurts mentors too, ngl
        - Code navigation, especially down into libs, is missing. Again, that's really rough for
          most students and mentors.
        - I know we have some fear about committing to AS, gradle errors, etc.
            - We need to address this head-on and build knowledge amongst the mentors to help here.
- auto-format on save?
    - heh, one option is to make a macro and rebind cmd-s to
      it: https://medium.com/nerd-for-tech/auto-format-code-in-android-studio-intellij-idea-1f0450ee44a3
    - Looks like the new version of AS actually includes customizable "on save actions". Let's see
      what that looks like.
- multiple users for github in AS?
    - I'd love to be able to tie commits to students on teams. Yea, everything can be "seatlth" or "
      one student always logged in", but there's some pride that comes from ownership of your work
      that gets missed there.
    - I think AS supports this, get w/ Sidd and experiment.

# Notes for setting up this repo

- Start with the standard FtcRobotController repo
    - We would fork this and have this be the tip of our work.
    - Should allow us to keep up with the official FTC code more easily.
    - https://github.com/FIRST-Tech-Challenge/FtcRobotController
    - My test fork is https://github.com/FIRST-Tech-Challenge/FtcRobotController
- Add FTCLib
    - https://github.com/FTCLib/FTCLib
    - Docs to add it to the project are in the main readme
    - Doesn't add anything special to teamcode
    - Vision
        - I didn't add the vision part yet, but it should be straightforward.
- Add RoadRunner
    - https://github.com/acmerobotics/road-runner
    - https://github.com/acmerobotics/road-runner-quickstart
    - After adding the deps, copy all the files from the quickstart's teamcode dir.
        - Use AS to move those down to a roadrunner subdir
            - The refactor will kick in automatically when you drag-and-drop to the subdir.
- Add OpenCV
    - https://github.com/OpenFTC/EasyOpenCV
- @TODO: more steps
    - setting up code formatting
    - setup for removing annoying pre-commit analysis
    - ???

# Resources

- https://github.com/Beta8397/virtual_robot
    
  