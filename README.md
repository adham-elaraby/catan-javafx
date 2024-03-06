# FOP-Projekt

- [Studierenden-Guide](https://wiki.tudalgo.org/)
- [Link to Public Repo (Upstream)](https://github.com/FOP-2324/FOP-2324-Projekt-Student)
- [Link to Moodle Page](https://moodle.informatik.tu-darmstadt.de/course/view.php?id=1469&sectionid=18783)

# Important Commands

## Quick Walkthrough

Pull -> Edit files -> git add -> commit -> push

1. Always pull before working, and before pushing
`git pull origin main`

2. After implementing a task or a couple of tasks e.g. H2.1 + H2.2 do the following:
`git status` to see the status of any untracked changes

3. Add all the files that you changed that need to be tracked
`git add example-file.java`

4. Commit with message
`git commit -m "Enter a discription of what task you did"`

5. Push.
`git push origin main`

## Commands

To Pull each others private changes
`git pull origin main`

To Push your commits to private
`git push origin main`

(Only for Adham) To Pull any changes from public
`git pull public main`

# Setup! DO NOT SKIP
## Install GitHub Client

### MacOS
1. `brew install gh` if you have Homebrew installed, otherwise download from the GitHub website
2. Login to GitHub with Terminal
3. run `gh auth login` and choose website login

Then clone the repo, and run the following commands in the directory:

### Optional, you don't need to run the next command (Adham will handle this)
- Add the public repo as public
`git remote add public https://github.com/FOP-2324/FOP-2324-Projekt-Student.git`

### Old instructions don't follow:
- Make git not change `build.gradle.kts` because we all have different tu-id's
`git update-index --assume-unchanged build.gradle.kts`

