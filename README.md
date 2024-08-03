<#assign
    AssetEntryLocalServiceUtil=serviceLocator.findService("com.liferay.asset.kernel.service.AssetEntryLocalService")
    DLFileEntryLocalService=serviceLocator.findService("com.liferay.document.library.kernel.service.DLFileEntryLocalService")
				 		isProvideFeedbackNeed=false
/>

<#assign currDate=.now?date />
<div class="Notification-content-wrap full-width">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <div class="notifications-page-header full-width">
                    <div class="row no-gutters">
                        <div class="col-10">
                            <h3 class="section-heading font-resized">${layout.getName(locale)}</h3>
                        </div>
                        <div class="col-2">
                            <div class="row-grid-view">
                                <div class="grid-view-icon-wrapper">
                                    <ul>
                                        <li class="row-view-icon" >
                                            <span class="common-icon-base-blue base-active">
                                                <img src="${themeDisplay.getPathThemeRoot()}/images/rbi-main/icon-row-view.svg"
                                                    class="rowView" alt="Row View" title="${languageUtil.get(locale,'list-view')}" tabindex="0"/>
                                            </span>
                                        </li>


                                        <li class="grid-view-icon" >
                                            <span class="common-icon-base-blue">
                                                <img src="${themeDisplay.getPathThemeRoot()}/images/rbi-main/icon-grid-view.svg"
                                                    class="gridView" alt="Grid View" title="${languageUtil.get(locale,'grid-view')}"  tabindex="0"/>
                                            </span>
                                        </li>

                                    </ul>

                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

            <div class="col-12">
                <div class="notifications-row-wrapper toogle-grid-row show-row-view">
                    <div class="row">

                        <#if entries?has_content>
                            <#list entries as curEntry>
															<#assign UUID="" groupId="" fileSize="" docURL="" />
                                <#assign assetEntry=AssetEntryLocalServiceUtil.fetchEntry(curEntry.getClassName(),
                                    curEntry.getClassPK()) />
                                <#assign viewURL=curEntry.getViewURL()?split('?')[0] />
                                <#if assetEntry.getClassName()=="com.liferay.journal.model.JournalArticle">
                                    <#assign article=assetEntry.getAssetRenderer().getArticle() id=article.getId()
																				warningMessage = article.getExpandoBridge().getAttribute("warning-message")		 
                                        groupId=article.getGroupId()
                                        URL=article.getExpandoBridge().getAttribute("pdf-url")
																				deadLineDate=article.getExpandoBridge().getAttribute("Feedback End Date")
																						 />
                                    <#assign content=saxReaderUtil.read(article.getContentByLocale(locale))
                                        notificationId=content.valueOf("//dynamic-element[@name='CopyOfNumeric76395357'
                                        ]/dynamic-content")
                                        description=content.valueOf("//dynamic-element[@name='CopyOfRichText18402484'
                                        ]/dynamic-content")
																						 publishDate=assetEntry.publishDate
																						 />
                                </#if>
                                <div class="col-12 grid-view-col">
                                    <div class="notification-row-each">
                                        <div class="notification-row-each-inner">
                                            <#if publishDate?has_content>
                                                <div class="notification-date font-resized"><span tabindex="0">${dateUtil.getDate(publishDate, "MMM dd, yyyy", locale)}</span>
                                                    <#if currDate?string==publishDate?date?string>
                                                        <div class="tag-new">
                                                            <span tabindex="0">${languageUtil.get(locale,'new')}</span>
                                                        </div>
                                                    </#if>
																										<#if warningMessage?has_content>
                                                        <div class="tag-withdrawn-date tag-new" >
                                                            <span tabindex="0">${warningMessage}</span>
                                                        </div>
                                                    </#if>
																									<#assign dateComparision=deadLineDate?datetime gt .now?datetime />
																									<#if dateComparision>
																									
																													<div class="item-tags" tabindex="0">${languageUtil.get(locale,'open-for-feedback')}</div>
																				
																									</#if>
                                                </div>
                                            </#if>
																							<a
                                                href="${viewURL}" class="mtm_list_item_heading">
                                                <div
                                                    class="notifications-heading c-tooltips position-relative">
                                                    <span class="mtm_list_item_heading truncatedContent font-resized">${assetEntry.getTitle(locale)}</span>
																										 <div class="custom-tooltip">
																												<div class="tooltip-arrow-up"></div>
																												<div class="tooltip-content font-resized">
																													${assetEntry.getSummary(locale)}
                                                				</div>
																											<div class="tooltip-content-hidden"  style="display:none">${assetEntry.getSummary(locale)}</div>
                                            				</div>
                                                </div>
																							  <div class="notifications-heading-hidden" style="display:none">${assetEntry.getTitle(locale)}</div>
                                            </a>
                                            	
																					
                                            <div class="notifications-description font-resized">
																							<#if assetEntry.getSummary(locale)?has_content >		
																								${assetEntry.getSummary(locale)}
																							</#if>
                                            </div>
																					<div class="notifications-description-hidden"  style="display:none">
																						<#if assetEntry.getSummary(locale)?has_content >		
																								${assetEntry.getSummary(locale)}
																							${locale}
																							</#if>
																					</div>
                                           
                                            <!--buttons-->
                                          
                                            <#if URL?has_content>
                                                <#list URL?keys as key>

                                                    <#if key==locale>
                                                        <#assign docURL=URL?values[key_index] />
                                                    </#if>
                                                </#list>
                                            </#if>
                                            <#if docURL?has_content>
                                                <#list docURL?split("/") as sValue>
                                                    <#if sValue?is_last>
                                                        <#list sValue?split("?") as uuid>
                                                            <#if uuid?is_first>
                                                                <#assign UUID=uuid />
                                                            </#if>
                                                        </#list>
                                                    </#if>
                                                </#list>
                                            </#if>

                                            <#if UUID?has_content>
                                               <#if groupId?has_content>
																									<#attempt>
																										<#assign DLFileEntry=DLFileEntryLocalService.fetchFileEntry(UUID,groupId?number)
																														fileSize=DLFileEntry.getSize()
																														fileAssetEntry=AssetEntryLocalServiceUtil.getEntry("com.liferay.document.library.kernel.model.DLFileEntry",DLFileEntry.getFileEntryId())
																														docURL=fileAssetEntry.getAssetRenderer().getURLDownload(themeDisplay) 
																														 docURL=docURL?replace("/"+UUID,"")
																														 />
																										<#recover>

																									</#attempt>

																								 </#if>
                                            </#if>
                                            <#if docURL?? && docURL?has_content>
                                                <div class="btn-wrap">
                                                <a href="${docURL?split('?')[0]}" target="_blank" class="matomo_download ">
                                                    <img src="/documents/70233/0/fileDownload.svg" class="downloadFile"
                                                        alt="${languageUtil.get(locale,'download')}" title="${languageUtil.get(locale,'download')}" >
                                                       PDF (<span class="fileSize">${fileSize}</span> )
                                                    </a>
                                                </div>
                                            </#if>
                                        </div>

                                    </div>
                                </div>
                            </#list>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
	
$( document ).ready(function() {

	$(".fileSize").each(function() {
		var	fileSize = $( this ).html()
		if(fileSize !=""){ 
			$(this).html(formatFileSize(parseInt(fileSize),2))
		}
	});

	function formatFileSize(bytes,decimalPoint) {
   if(bytes == 0) 
		 return '0 Bytes';
   	var k = 1024,
       dm = decimalPoint || 2,
       sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
       i = Math.floor(Math.log(bytes) / Math.log(k));
   return (parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i]);
	}
});
</script>
