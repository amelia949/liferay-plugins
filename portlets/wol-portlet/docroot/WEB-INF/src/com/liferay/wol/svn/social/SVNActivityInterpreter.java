/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.wol.svn.social;

import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityFeedEntry;
import com.liferay.wol.model.SVNRepository;
import com.liferay.wol.model.SVNRevision;
import com.liferay.wol.service.SVNRevisionLocalServiceUtil;

/**
 * <a href="SVNActivityInterpreter.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class SVNActivityInterpreter extends BaseSocialActivityInterpreter {

	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	protected SocialActivityFeedEntry doInterpret(
			SocialActivity activity, ThemeDisplay themeDisplay)
		throws Exception {

		String creatorUserName = getUserName(
			activity.getUserId(), themeDisplay);

		int activityType = activity.getType();

		// Title

		SVNRevision svnRevision = SVNRevisionLocalServiceUtil.getSVNRevision(
			activity.getClassPK());

		SVNRepository svnRepository = svnRevision.getSVNRepository();

		String title = StringPool.BLANK;

		if (activityType == SVNActivityKeys.ADD_REVISION) {
			title = themeDisplay.translate(
				"activity-wol-svn-add-revision",
				new Object[] {
					creatorUserName,
					String.valueOf(svnRevision.getRevisionNumber()),
					svnRepository.getShortURL()
				});
		}

		// Body

		StringMaker sm = new StringMaker();

		sm.append("<a href=\"");
		sm.append(svnRevision.getWebRevisionNumberURL());
		sm.append("\" target=\"_blank\">");
		sm.append(svnRevision.getComments());
		sm.append("</a>");

		String body = sm.toString();

		return new SocialActivityFeedEntry(title, body);
	}

	private static final String[] _CLASS_NAMES = new String[] {
		SVNRevision.class.getName()
	};

}