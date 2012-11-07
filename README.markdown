    Author: Christophe Carvalho Vilas-Boas <christophe.carvalhovilasboas@gmail.com>

    Copyright (c) 2012 Christophe Carvalho Vilas-Boas.
    All rights reserved.

    Permission is hereby granted, free of charge, to any person
    obtaining a copy of this software and associated documentation
    files (the "Software"), to deal in the Software without
    restriction, including without limitation the rights to use,
    copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the
    Software is furnished to do so, subject to the following
    conditions:

    The above copyright notice and this permission notice shall be
    included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
    OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
    NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
    HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
    WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
    OTHER DEALINGS IN THE SOFTWARE.

# Everything started here

http://stackoverflow.com/questions/6923044/java-library-using-css-selectors-to-parse-xml

*And here is the code !*

# Main features of JavaXMLQuery

- Parse XML
- Read XML
- Manipulate XML

# BRANCHES:
The branching structure follows the git flow concept, defined by Vincent Driessen: http://nvie.com/posts/a-successful-git-branching-model/

* Master branch:

	The main branch where the source code of HEAD always reflects a production-ready state.

* Develop branch:

	Consider this to be the main branch where the source code of HEAD always reflects a state with the latest delivered development changes for the next release. Some would call this the “integration branch”.

* Feature branches:

	These are used to develop new features for the upcoming or a distant future release. The essence of a feature branch is that it exists as long as the feature is in development, but will eventually be merged back into develop (to definitely add the new feature to the upcoming release) or discarded (in case of a disappointing experiment).

* Release branches:

	These branches support preparation of a new production release. By using this, the develop branch is cleared to receive features for the next big release.

* Hotfix branches:

	Hotfix branches are very much like release branches in that they are also meant to prepare for a new production release, albeit unplanned.